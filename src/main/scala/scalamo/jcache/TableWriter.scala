package scalamo.jcache

import scalamo.mapping.ItemMarshaller

abstract class TableWriter[K, V](implicit keyMapper: KeyMapper[K], itemMarshaller: ItemMarshaller[V]) {
  import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
  import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Table}

  val tableName: String

  lazy val dynamoDB: DynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient())
  lazy val table: Table = dynamoDB.getTable(tableName)

  def write(key: K, value: V): Unit =
    table.putItem(itemMarshaller(value).withPrimaryKey(keyMapper(key)))

  def writeAll(entries: Set[(K, V)]): Unit = ???

  def delete(key: K): Unit =
    table.deleteItem(keyMapper(key))

  def deleteAll(keys: Set[K]): Unit = ???
}
