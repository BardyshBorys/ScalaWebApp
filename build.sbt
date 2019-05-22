name := "ScalaWebApp"


version := "0.1"

scalaVersion := "2.12.8"


(scalastyleConfig in Test) := baseDirectory.value / "scalastyle-test-config.xml"
(scalastyleConfigUrl in Test) := Some(url("http://www.scalastyle.org/scalastyle_config.xml"))

lazy val dependencies =
  new {
    val scalatestV = "3.0.4"
    val scalacheckV = "1.13.5"
    val scoptV = "3.7.1"
    val catsV = "1.6.0"
    val akkaActorV = "2.5.20"
    val akkaStreamV = "2.5.20"
    val akkaHttpV = "10.1.7"
    val typeSafeSlickV = "3.3.0"
    val slf4jV = "1.6.4"
    val slickV = "3.3.0"
    val typeSafeConfigV = "1.3.4"
    val h2databaseV = "1.4.196"
    val akkaSprayJsonV = "10.1.8"
    val streamTestkitV = "2.5.19"
    val httpTestkitV = "10.1.8"

    val scalatest = "org.scalatest" %% "scalatest" % scalatestV
    val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckV
    val scopt = "com.github.scopt" %% "scopt" % scoptV
    val cats = "org.typelevel" %% "cats-core" % catsV
    val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaActorV
    val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaStreamV
    val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpV
    val typeSafeSlick = "com.typesafe.slick" %% "slick" % typeSafeSlickV
    val slf4j = "org.slf4j" % "slf4j-nop" % slf4jV
    val slick = "com.typesafe.slick" %% "slick-hikaricp" % slickV
    val typeSafeConfig = "com.typesafe" % "config" % typeSafeConfigV
    val slickCodegen = "com.typesafe.slick" %% "slick-codegen" % typeSafeSlickV
    val h2database = "com.h2database" % "h2" % h2databaseV
    val akkaSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaSprayJsonV
    val streamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % streamTestkitV
    val httpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % httpTestkitV

  }

lazy val commonDependencies = Seq(
  dependencies.scalatest % "test",
  dependencies.scalacheck % "test",
  dependencies.scopt,
  dependencies.cats,
  dependencies.akkaActor,
  dependencies.akkaHttp,
  dependencies.akkaStream,
  dependencies.typeSafeSlick,
  dependencies.slf4j,
  dependencies.slick,
  dependencies.typeSafeConfig,
  dependencies.slickCodegen,
  dependencies.h2database,
  dependencies.akkaSprayJson,
  dependencies.streamTestkit,
  dependencies.httpTestkit
)

libraryDependencies ++= commonDependencies
