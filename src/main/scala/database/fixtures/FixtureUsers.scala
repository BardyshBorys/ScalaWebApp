package database


import database.models.User.{users, _}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


package object FixtureUsers {
  private val h2db = Database.forConfig("h2mem1")

  lazy val inserts =
    DBIO.seq(
      users += User(101, "Ash Ketchum", Option("99 Market Street"), "aketchum@gmail.com"),
      users += User(49,  "Brock",       Option("1 Party Place"),    "dbrok@gmail.com"   ),
      users += User(150, "Misty",       Option("100 Coffee Lane"),  "AMisty@gmail.com"  ),
    )

  def patch(): Unit = {

    Await.result(h2db.run(DBIO.seq(
      users.schema.create,
      inserts
    )), Duration.Inf)
  }
}
