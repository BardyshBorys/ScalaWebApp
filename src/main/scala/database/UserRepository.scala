package database

import database.models.User.{User, users}
import slick.jdbc.H2Profile.api._



trait UserRepositoryTrait[F[_]] {

  def registerUser(user: User): F[User]

  def getById(id: Long): F[Option[User]]

  def getByUsername(username: String): F[Option[User]]

}

class UserRepository extends UserRepositoryTrait[DBIO] {

  override def registerUser(user: User): DBIO[User] = Query.writeUser += user

  override def getById(id: Long): DBIO[Option[User]] = Query.userById(id).result.headOption

  override def getByUsername(username: String): DBIO[Option[User]] = Query.userByUserName(username).result.headOption

  object Query {
    val writeUser = users returning users
      .map(_.id) into((User, id) => User.copy(id))

    val userById = users.findBy(_.id)
    val userByUserName = users.findBy(_.username)

  }

}
