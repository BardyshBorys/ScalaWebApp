import database.FixtureUsers
import database.models.User.{users, User}
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration.Duration



class DatabaseTest extends FunSuite with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    super.beforeAll()
    FixtureUsers.patch()
  }

  test("Integration") {
    import slick.jdbc.H2Profile.api._

    val h2db = Database.forConfig("h2mem1")

    val query = users.map(p => p.username)
    val usernames: Seq[String] = Await.result(
      h2db.run(query.result),
      Duration.Inf
    ).sorted
    val standard  = Vector(
      "Ash Ketchum",
      "Brock",
      "Misty",
    )
    assert(standard == usernames)
  }

  test("UserReporitory") {
    import database.UserRepository

    import slick.jdbc.H2Profile.api._

    val h2db = Database.forConfig("h2mem1")
    val userRepo = new UserRepository()
    val result = h2db.run(userRepo.getById(1))
    val standard  = Some(User(1, "Ash Ketchum", Option("99 Market Street"), "aketchum@gmail.com"))
    assert(standard === Await.result(result, Duration.Inf))
  }

  test("UserService") {
    import database.UserRepository
    import database.UserService
    val userRepo = new UserRepository()
    val userService = new UserService(userRepo)
    val testUser = ("test", Option("test"), "test@gmail.com")
    val insertResult = Await.result(userService.registerUser("test", Option("test"), "test@gmail.com"), Duration.Inf)
    val result = userService.getById(insertResult.id)
    assert(Some(User(4, "test", Option("test"), "test@gmail.com")) === Await.result(result, Duration.Inf))
  }
}