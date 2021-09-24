package scalamo.mapping

import com.amazonaws.services.dynamodbv2.document.Item

trait AttributeMarshaller[A] {
  def apply(item: Item, key: String, value: A): Item
}

object AttributeMarshaller extends AttributeMarshallerFunctions with AttributeMarshallers

trait AttributeMarshallerFunctions {
  def apply[A](implicit marshaller: AttributeMarshaller[A]): AttributeMarshaller[A] =
    marshaller
}

trait AttributeMarshallers {
  import java.nio.ByteBuffer
  import java.time.format.DateTimeFormatter
  import java.time.{Instant, ZonedDateTime}
  import scala.jdk.CollectionConverters._

  implicit val bigDecimalMarshaller: AttributeMarshaller[BigDecimal] =
    (item: Item, key: String, value: BigDecimal) => item.withNumber(key, value.bigDecimal)

  implicit val bigDecimalSetMarshaller: AttributeMarshaller[Set[BigDecimal]] =
    (item: Item, key: String, value: Set[BigDecimal]) =>
      item.withBigDecimalSet(key, value.map(_.bigDecimal).asJava)

  implicit val bigIntMarshaller: AttributeMarshaller[BigInt] =
    (item: Item, key: String, value: BigInt) => item.withBigInteger(key, value.bigInteger)

  implicit val binaryMarshaller: AttributeMarshaller[Array[Byte]] =
    (item: Item, key: String, value: Array[Byte]) => item.withBinary(key, value)

  implicit val binarySetMarshaller: AttributeMarshaller[Set[Array[Byte]]] =
    (item: Item, key: String, value: Set[Array[Byte]]) => item.withBinarySet(key, value.asJava)

  implicit val booleanMarshaller: AttributeMarshaller[Boolean] =
    (item: Item, key: String, value: Boolean) => item.withBoolean(key, value)

  implicit val byteBufferMarshaller: AttributeMarshaller[ByteBuffer] =
    (item: Item, key: String, value: ByteBuffer) => item.withBinary(key, value)

  implicit val byteBufferSetMarshaller: AttributeMarshaller[Set[ByteBuffer]] =
    (item: Item, key: String, value: Set[ByteBuffer]) => item.withByteBufferSet(key, value.asJava)

  implicit val doubleMarshaller: AttributeMarshaller[Double] =
    (item: Item, key: String, value: Double) => item.withDouble(key, value)

  implicit val floatMarshaller: AttributeMarshaller[Float] =
    (item: Item, key: String, value: Float) => item.withFloat(key, value)

  implicit val intMarshaller: AttributeMarshaller[Int] =
    (item: Item, key: String, value: Int) => item.withInt(key, value)

  implicit def listMarshaller[A]: AttributeMarshaller[List[A]] =
    (item: Item, key: String, value: List[A]) => item.withList(key, value.asJava)

  implicit def seqMarshaller[A]: AttributeMarshaller[Seq[A]] =
    (item: Item, key: String, value: Seq[A]) => item.withList(key, value.asJava)

  implicit val longMarshaller: AttributeMarshaller[Long] =
    (item: Item, key: String, value: Long) => item.withLong(key, value)

  implicit def mapMarshaller[V]: AttributeMarshaller[Map[String, V]] =
    (item: Item, key: String, value: Map[String, V]) => item.withMap(key, value.asJava)

  implicit val numberMarshaller: AttributeMarshaller[Number] =
    (item: Item, key: String, value: Number) => item.withNumber(key, value)

  implicit val numberSetMarshaller: AttributeMarshaller[Set[Number]] =
    (item: Item, key: String, value: Set[Number]) => item.withNumberSet(key, value.asJava)

  implicit val shortMarshaller: AttributeMarshaller[Short] =
    (item: Item, key: String, value: Short) => item.withShort(key, value)

  implicit val stringMarshaller: AttributeMarshaller[String] =
    (item: Item, key: String, value: String) => item.withString(key, value)

  implicit val stringSetMarshaller: AttributeMarshaller[Set[String]] =
    (item: Item, key: String, value: Set[String]) => item.withStringSet(key, value.asJava)

  implicit val zonedDateTimeMarshaller: AttributeMarshaller[ZonedDateTime] =
    (item: Item, key: String, value: ZonedDateTime) =>
      item.withString(key, value.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))

  implicit val instantMarshaller: AttributeMarshaller[Instant] =
    (item: Item, key: String, value: Instant) => item.withLong(key, value.toEpochMilli)

  implicit def optionMarshaller[A](implicit marshaller: AttributeMarshaller[A]): AttributeMarshaller[Option[A]] =
    (item: Item, key: String, value: Option[A]) => value match {
      case Some(v) =>
        marshaller(item, key, v)
      case None =>
        item.withNull(key)
    }
}
