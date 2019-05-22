
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import database.models.User.User
import database.{UserRepository, UserService}
import spray.json.DefaultJsonProtocol

trait UserJsonProtocol extends DefaultJsonProtocol {
  implicit val templateFormat = jsonFormat4(User)
}

object UserJsonProtocol extends UserJsonProtocol


object UserRoutes {
  val userRepo = new UserRepository()
  val userServ = new UserService(userRepo)

  import UserJsonProtocol._

  val routes =
    pathPrefix("users") {
      post {
        entity(as[User]) { user =>
          onSuccess(userServ.registerUser(user.username, user.address, user.email)) { performed =>
            val id = performed.id
            complete(
              StatusCodes.Created,
              HttpEntity(ContentTypes.`application/json`, s"""{"userId": $id}""")
            )
          }
        }
      } ~
        get {
          parameters('id.as[Long]) { userId =>
            // there might be no item for a given id
            val possibleUser: concurrent.Future[Option[User]] = userServ.getById(userId)

            onSuccess(possibleUser) {
              case Some(item) => complete(item)
              case None => complete(StatusCodes.NotFound)
            }
          }
        }
    }
}
