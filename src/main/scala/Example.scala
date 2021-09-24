import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import java.time.Instant
import scalamo._

case class User(userId: String, name: String, lastLogin: Instant)

object Example extends App {
  val dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient())
  val table = dynamoDB.getTable("users")
  val maybeUser = table.get[User]("userId", "12345") // returns scalamo.Validated[User], which is cats.data.ValidatedNel[Throwable, User]

  maybeUser.foreach { user =>
    table.put(user.copy(lastLogin = Instant.now()))  // Update the user's lastLogin value
  }
}
