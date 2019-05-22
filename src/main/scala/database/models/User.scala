package database.models


import slick.jdbc.H2Profile.api._

object User {

  case class User(id: Long, username: String, address: Option[String], email: String)


  class Users(tag: Tag) extends Table[User](tag, "Users") {

    def id = column[Long]("USER_ID", O.PrimaryKey, O.AutoInc)

    def username = column[String]("USER_NAME")

    def address = column[Option[String]]("ADDRESS")

    def email = column[String]("EMAIL")

    def * = (id, username, address, email) <> (User.tupled, User.unapply)
  }

  lazy val users = TableQuery[Users]

}
