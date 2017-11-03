name := "JSON-Validation-Service-WI2017"

version := "0.1"

scalaVersion := "2.12.4"

val circeVersion = "0.9.0-M1"

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