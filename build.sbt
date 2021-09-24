name := "scalamo"

version := "0.1.0"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.12.73",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.typelevel" %% "cats-core" % "2.3.0",
  "javax.cache" % "cache-api" % "1.1.1",
  "org.scalatest" %% "scalatest" % "3.2.9" % "test"
)
