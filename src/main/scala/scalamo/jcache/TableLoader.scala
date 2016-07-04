package scalamo.jcache

import scalamo._
import scalamo.mapping.ItemUnmarshaller

abstract class TableLoader[K, V](implicit keyMapper: KeyMapper[K], itemUnmarshaller: ItemUnmarshaller[V]) {
  import com.amazonaws.auth.profile.ProfileCredentialsProvider
  import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
  import com.amazonaws.services.dynamodbv2.document.DynamoDB

  val tableName: String

  lazy val dynamoDB = new DynamoDB(new AmazonDynamoDBClient(new ProfileCredentialsProvider()))
  lazy val table = dynamoDB.getTable(tableName)

  def load(key: K): Validated[V] =
    itemUnmarshaller(table.getItem(keyMapper(key)))

  def loadAll(keys: Set[K]): Map[K, V] = ???
}
