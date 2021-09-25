import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import java.time.Instant
import scalamo.{Table, Validated}

case class User(userId: String, name: String, lastLogin: Instant)

object Example extends App {
  val dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient())
  val table = Table[User](dynamoDB, "users")

  val users = Seq(
    User("12345", "Mark Corrigan", Instant.now()),
    User("23456", "Jeremy Usborne", Instant.now()),
    User("34567", "Super Hans", Instant.now())
  )

  // batch write users
  table.put(users)

  // retrieve one user
  val maybeUser: Validated[User] = table.get("userId", "12345")
  maybeUser.foreach { user =>
    // Update the user's lastLogin value
    table.put(user.copy(lastLogin = Instant.now()))
  }

  // batch get users
  val maybeUsers: Validated[Seq[User]] = table.get("userId", Seq("23456", "34567"))
  maybeUsers.foreach { users =>
    users.foreach(println)
  }
}
