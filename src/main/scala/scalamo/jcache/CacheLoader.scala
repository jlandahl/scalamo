package scalamo.jcache

import scalamo.mapping.ItemUnmarshaller

abstract class CacheLoader[K, V](implicit keyMapper: KeyMapper[K], itemUnmarshaller: ItemUnmarshaller[V])
  extends javax.cache.integration.CacheLoader[K, V]
{
  import cats.data.Validated.{Invalid, Valid}
  import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
  import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Table}

  def tableName: String

  lazy val dynamoDB: DynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient())
  lazy val table: Table = dynamoDB.getTable(tableName)

  override def load(key: K): V =
    itemUnmarshaller(table.getItem(keyMapper(key))) match {
      case Valid(value) =>
        value
      case Invalid(errors) =>
        throw CompoundException(errors)
    }

  override def loadAll(keys: java.lang.Iterable[_ <: K]): java.util.Map[K, V] = ???
}
