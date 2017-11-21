name := "JSON-Validation-Service-WI2017"

version := "0.1"

scalaVersion := "2.12.4"

val circeVersion = "0.9.0-M1"

addCommandAlias("dist", ";clean;compile")

libraryDependencies ++= Seq(
  //ScalaTest
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "junit" % "junit" % "4.10" % Test,

  //Finch
  "com.github.finagle" %% "finch-core" % "0.16.0-M3",
  "com.github.finagle" %% "finch-circe" % "0.16.0-M3",
  "com.twitter" %% "twitter-server" % "17.10.0",

  //Schema validator
  "com.github.java-json-tools" % "json-schema-validator" % "2.2.8",
)

//Circe
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

//Slick & H2
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
  "com.typesafe" % "config" % "1.3.1",
  "com.h2database" % "h2" % "1.4.196",
  "com.zaxxer" % "HikariCP" % "2.3.3"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v", "-s", "-a")

parallelExecution in Test := false