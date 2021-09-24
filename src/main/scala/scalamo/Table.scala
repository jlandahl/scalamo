package scalamo

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import scalamo.mapping.{ItemMarshaller, ItemUnmarshaller}

case class Table[A](dynamoDB: DynamoDB, tableName: String,
                    projectionExpression: Option[String] = None,
                    nameMap: Option[Map[String, String]] = None)
                   (implicit marshaller: ItemMarshaller[A], unmarshaller: ItemUnmarshaller[A]) {
  import cats.syntax.validated._
  import cats.syntax.either._
  import com.amazonaws.services.dynamodbv2.document.{KeyAttribute, PrimaryKey, Table => DynamoTable}
  import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec
  import scala.jdk.CollectionConverters._
  import scala.util.Either

  val table: DynamoTable = dynamoDB.getTable(tableName)

  def get(hashKeyName: String, hashKeyValue: Any): Validated[A] =
    try {
      val item = (projectionExpression, nameMap) match {
        case (Some(expr), Some(map)) =>
          table.getItem(hashKeyName, hashKeyValue, expr, map.asJava)
        case (_, _) =>
          table.getItem(hashKeyName, hashKeyValue)
      }
      unmarshaller(item)
    } catch {
      case scala.util.control.NonFatal(t) =>
        t.invalidNel
    }

  def get(hashKeyName: String, hashKeyValue: Any,
          rangeKeyName: String, rangeKeyValue: Any): Validated[A] =
    try {
      val item = (projectionExpression, nameMap) match {
        case (Some(expr), Some(map)) =>
          table.getItem(hashKeyName, hashKeyValue, rangeKeyName, rangeKeyValue, expr, map.asJava)
        case (_, _) =>
          table.getItem(hashKeyName, hashKeyValue, rangeKeyName, rangeKeyValue)
      }
      unmarshaller(item)
    } catch {
      case scala.util.control.NonFatal(t) =>
        t.invalidNel
    }

  def get(primaryKeyComponents: KeyAttribute*): Validated[A] =
    try {
      unmarshaller(table.getItem(primaryKeyComponents: _*))
    } catch {
      case scala.util.control.NonFatal(t) =>
        t.invalidNel
    }

  def get(primaryKey: PrimaryKey): Validated[A] =
    try {
      unmarshaller(table.getItem(primaryKey))
    } catch {
      case scala.util.control.NonFatal(t) =>
        t.invalidNel
    }

  def getItem(spec: GetItemSpec): Validated[A] =
    try {
      unmarshaller(table.getItem(spec))
    } catch {
      case scala.util.control.NonFatal(t) =>
        t.invalidNel
    }

  def scan(filterExpression: String, valueMap: Map[String, AnyRef]): Either[Throwable, Iterator[Validated[A]]] =
    Either catchNonFatal {
      val results = (projectionExpression, nameMap) match {
        case (Some(pe), Some(nm)) =>
          table.scan(filterExpression, pe, nm.asJava, valueMap.asJava)
        case (None, Some(nm)) =>
          table.scan(filterExpression, nm.asJava, valueMap.asJava)
        case (_, _) =>
          table.scan(filterExpression, null, valueMap.asJava)
      }
      results.iterateAs[A]
    }
}
