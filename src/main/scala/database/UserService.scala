package database

import java.awt.print.Book
import java.util.concurrent.{ArrayBlockingQueue, ThreadPoolExecutor, TimeUnit}

import cats.Monad
import database.models.User.User
import slick.jdbc.H2Profile.api._

import scala.concurrent.{ExecutionContext, Future}

class UserService[F[_]](repository: UserRepositoryTrait[DBIO])(implicit monad: Monad[F]) {

  private var incremental_id: Long = 0
  private val h2db = Database.forConfig("h2mem1")
    private val numWorkers = 10
    val queueCapacity = 10
    implicit val ec = ExecutionContext.fromExecutorService(
      new ThreadPoolExecutor(
        numWorkers, numWorkers,
        10, TimeUnit.SECONDS,
        new ArrayBlockingQueue[Runnable](queueCapacity) {
          override def offer(e: Runnable) = {
            put(e); // may block if waiting for empty room
            true
          }
        }
      )
    )

  def registerUser(username: String, address: Option[String], email: String): Future[User] = {
    incremental_id += 1
    val user = User(incremental_id, username, address, email)
    val action = for {
      user <- repository.registerUser(user)
    } yield user
    h2db.run(action)
  }

  def getByUsername(username: String): Future[Option[User]] = {
    h2db.run(repository.getByUsername(username))
  }

  def getById(id: Long): Future[Option[User]] = {
    h2db.run(repository.getById(id))
  }

}