import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{ContentTypes, MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import UserRoutes.routes
import akka.http.scaladsl.marshalling.Marshal
import akka.event.NoLogging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import org.scalatest._
import database.FixtureUsers
import database.models.User.User
import org.scalatest.concurrent.ScalaFutures


class RouteTest extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {
  override def beforeAll(): Unit = {
    super.beforeAll()
    FixtureUsers.patch()
  }

  "The service" should {

    "return a JSON for GET requests to the '/users?id=1' path" in {
      Get("/users?id=1") ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"address":"99 Market Street","email":"aketchum@gmail.com","id":1,"username":"Ash Ketchum"}""")
      }
    }

    "return a greeting for POST requests to the root path" in {
      val jsonRequest = ByteString(
        s"""
           |{
           |"id": 1,
           |"username":"LOL",
           |"address":"FOO",
           |"email":"lolfoo@gmail.com"
           |}
        """.stripMargin)
      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/users",
        entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

      postRequest ~> routes ~> check {
        status should ===(StatusCodes.Created)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"userId": 4}""")
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      // tests:
      Put() ~> Route.seal(routes) ~> check {
        status shouldEqual StatusCodes.NotFound
        responseAs[String] shouldEqual "The requested resource could not be found."
      }
    }
  }
}
