package scalamo.examples

import cats.data.Validated.{Invalid, Valid}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import java.time.Instant
import scalamo.{Table, Validated}

case class User(userId: String, name: String, lastLogin: Instant)

object Example extends App {
  val dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient())
  val table = Table[User](dynamoDB, "users")

  val users = Seq(
    User("mark-o", "Mark Corrigan", Instant.now()),
    User("jez", "Jeremy Usborne", Instant.now()),
    User("superhans", "Super Hans", Instant.now())
  )

  // batch write users
  table.put(users)

  // retrieve one user
  val maybeUser: Option[Validated[User]] = table.get("userId", "mark-o")
  maybeUser match {
    case Some(Valid(user)) =>
      table.put(user.copy(lastLogin = Instant.now()))
      println("Updated user")
    case Some(Invalid(errors)) =>
      println(s"Errors parsing user item: $errors")
    case None =>
      println("Not found")
  }

  // batch get users
  val maybeUsers: Validated[Seq[User]] = table.get("userId", Seq("jez", "superhans", "sophie"))
  maybeUsers.foreach { users =>
    users.foreach(println)
  }
}
