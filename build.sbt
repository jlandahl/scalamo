name := "scalamo"

version := "0.1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.13",
  "com.chuusai" %% "shapeless" % "2.3.1",
  "org.typelevel" %% "cats" % "0.6.0",
  "javax.cache" % "cache-api" % "1.0.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)
