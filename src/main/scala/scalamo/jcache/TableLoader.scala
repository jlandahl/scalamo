package scalamo.jcache

import scalamo._
import scalamo.mapping.ItemUnmarshaller

abstract class TableLoader[K, V](implicit keyMapper: KeyMapper[K], itemUnmarshaller: ItemUnmarshaller[V]) {
  import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
  import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Table}

  val tableName: String

  lazy val dynamoDB: DynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient())
  lazy val table: Table = dynamoDB.getTable(tableName)

  def load(key: K): Validated[V] =
    itemUnmarshaller(table.getItem(keyMapper(key)))

  def loadAll(keys: Set[K]): Map[K, V] = ???
}
