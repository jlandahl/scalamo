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
  val maybeUser: Validated[User] = table.get("userId", "mark-o")
  maybeUser.foreach { user =>
    // Update the user's lastLogin value
    table.put(user.copy(lastLogin = Instant.now()))
  }

  // batch get users
  val maybeUsers: Validated[Seq[User]] = table.get("userId", Seq("jez", "superhans"))
  maybeUsers.foreach { users =>
    users.foreach(println)
  }
}
