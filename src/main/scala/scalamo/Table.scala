package scalamo

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import scalamo.mapping.{ItemMarshaller, ItemUnmarshaller}

case class Table[A](dynamoDB: DynamoDB, tableName: String,
                    projectionExpression: Option[String] = None,
                    nameMap: Option[Map[String, String]] = None)
                   (implicit marshaller: ItemMarshaller[A], unmarshaller: ItemUnmarshaller[A]) {
  import cats.syntax.validated._
  import cats.syntax.either._
  import com.amazonaws.services.dynamodbv2.document.{BatchWriteItemOutcome, KeyAttribute, PrimaryKey, PutItemOutcome, Table => DynamoTable}
  import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec
  import scala.jdk.CollectionConverters._
  import scala.util.Either

  val table: DynamoTable = dynamoDB.getTable(tableName)

  def get[B](hashKeyName: String, hashKeyValue: B): Option[Validated[A]] =
    try {
      val item = (projectionExpression, nameMap) match {
        case (Some(expr), Some(map)) =>
          table.getItem(hashKeyName, hashKeyValue, expr, map.asJava)
        case (_, _) =>
          table.getItem(hashKeyName, hashKeyValue)
      }
      Option(item).map(unmarshaller(_))
    } catch {
      case scala.util.control.NonFatal(t) =>
        Some(t.invalidNel)
    }

  def get[B, C](hashKeyName: String, hashKeyValue: B,
          rangeKeyName: String, rangeKeyValue: C): Option[Validated[A]] =
    try {
      val item = (projectionExpression, nameMap) match {
        case (Some(expr), Some(map)) =>
          table.getItem(hashKeyName, hashKeyValue, rangeKeyName, rangeKeyValue, expr, map.asJava)
        case (_, _) =>
          table.getItem(hashKeyName, hashKeyValue, rangeKeyName, rangeKeyValue)
      }
      Option(item).map(unmarshaller(_))
    } catch {
      case scala.util.control.NonFatal(t) =>
        Some(t.invalidNel)
    }

  def get(primaryKeyComponents: KeyAttribute*): Option[Validated[A]] =
    try {
      val item = table.getItem(primaryKeyComponents: _*)
      Option(item).map(unmarshaller(_))
    } catch {
      case scala.util.control.NonFatal(t) =>
        Some(t.invalidNel)
    }

  def get(primaryKey: PrimaryKey): Option[Validated[A]] =
    try {
      val item = table.getItem(primaryKey)
      Option(item).map(unmarshaller(_))
    } catch {
      case scala.util.control.NonFatal(t) =>
        Some(t.invalidNel)
    }

  def get(spec: GetItemSpec): Option[Validated[A]] =
    try {
      val item = table.getItem(spec)
      Option(item).map(unmarshaller(_))
    } catch {
      case scala.util.control.NonFatal(t) =>
        Some(t.invalidNel)
    }

  def get[B](hashKeyName: String, hashKeyValues: Seq[B]): Validated[Seq[A]] = {
    try {
      dynamoDB.get(tableName, hashKeyName, hashKeyValues: _*)
    } catch {
      case scala.util.control.NonFatal(t) =>
        t.invalidNel
    }
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

  def put(a: A): PutItemOutcome =
    table.put(a)

  def put(as: Iterable[A]): BatchWriteItemOutcome =
    dynamoDB.put(tableName, as)
}
