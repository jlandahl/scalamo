import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import java.time.Instant
import scalamo._

case class User(userId: String, name: String, lastLogin: Instant)

object Example extends App {
  val dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient())
  val table = dynamoDB.getTable("users")

  val users = Seq(
    User("12345", "Mark Corrigan", Instant.now()),
    User("23456", "Jeremy Usborne", Instant.now()),
    User("34567", "Super Hans", Instant.now())
  )

  // batch write users
  dynamoDB.put("users", users)

  // retrieve one user
  val maybeUser = table.get[User]("userId", "12345") // returns scalamo.Validated[User], which is cats.data.ValidatedNel[Throwable, User]
  maybeUser.foreach { user =>
    // Update the user's lastLogin value
    table.put(user.copy(lastLogin = Instant.now()))
  }
}
