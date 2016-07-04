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
  import scala.collection.JavaConverters._

  implicit val bigDecimalMarshaller =
    new AttributeMarshaller[BigDecimal] {
      def apply(item: Item, key: String, value: BigDecimal): Item =
        item.withNumber(key, value.bigDecimal)
    }

  implicit val bigDecimalSetMarshaller =
    new AttributeMarshaller[Set[BigDecimal]] {
      def apply(item: Item, key: String, value: Set[BigDecimal]): Item =
        item.withBigDecimalSet(key, value.map(_.bigDecimal).asJava)
    }

  implicit val bigIntMarshaller =
    new AttributeMarshaller[BigInt] {
      def apply(item: Item, key: String, value: BigInt): Item =
        item.withBigInteger(key, value.bigInteger)
    }

  implicit val binaryMarshaller =
    new AttributeMarshaller[Array[Byte]] {
      def apply(item: Item, key: String, value: Array[Byte]): Item =
        item.withBinary(key, value)
    }

  implicit val binarySetMarshaller =
    new AttributeMarshaller[Set[Array[Byte]]] {
      def apply(item: Item, key: String, value: Set[Array[Byte]]): Item =
        item.withBinarySet(key, value.asJava)
    }

  implicit val booleanMarshaller =
    new AttributeMarshaller[Boolean] {
      def apply(item: Item, key: String, value: Boolean): Item =
        item.withBoolean(key, value)
    }

  implicit val byteBufferMarshaller =
    new AttributeMarshaller[ByteBuffer] {
      def apply(item: Item, key: String, value: ByteBuffer): Item =
        item.withBinary(key, value)
    }

  implicit val byteBufferSetMarshaller =
    new AttributeMarshaller[Set[ByteBuffer]] {
      def apply(item: Item, key: String, value: Set[ByteBuffer]): Item =
        item.withByteBufferSet(key, value.asJava)
    }

  implicit val doubleMarshaller =
    new AttributeMarshaller[Double] {
      def apply(item: Item, key: String, value: Double): Item =
        item.withDouble(key, value)
    }

  implicit val floatMarshaller =
    new AttributeMarshaller[Float] {
      def apply(item: Item, key: String, value: Float): Item =
        item.withFloat(key, value)
    }

  implicit val intMarshaller =
    new AttributeMarshaller[Int] {
      def apply(item: Item, key: String, value: Int): Item =
        item.withInt(key, value)
    }

  implicit def listMarshaller[A] =
    new AttributeMarshaller[List[A]] {
      def apply(item: Item, key: String, value: List[A]): Item =
        item.withList(key, value.asJava)
    }

  implicit def seqMarshaller[A] =
    new AttributeMarshaller[Seq[A]] {
      def apply(item: Item, key: String, value: Seq[A]): Item =
        item.withList(key, value.asJava)
    }

  implicit val longMarshaller =
    new AttributeMarshaller[Long] {
      def apply(item: Item, key: String, value: Long): Item =
        item.withLong(key, value)
    }

  implicit def mapMarshaller[V] =
    new AttributeMarshaller[Map[String, V]] {
      def apply(item: Item, key: String, value: Map[String, V]): Item =
        item.withMap(key, value.asJava)
    }

  implicit val numberMarshaller =
    new AttributeMarshaller[Number] {
      def apply(item: Item, key: String, value: Number): Item =
        item.withNumber(key, value)
    }

  implicit val numberSetMarshaller =
    new AttributeMarshaller[Set[Number]] {
      def apply(item: Item, key: String, value: Set[Number]): Item =
        item.withNumberSet(key, value.asJava)
    }

  implicit val shortMarshaller =
    new AttributeMarshaller[Short] {
      def apply(item: Item, key: String, value: Short): Item =
        item.withShort(key, value)
    }

  implicit val stringMarshaller =
    new AttributeMarshaller[String] {
      def apply(item: Item, key: String, value: String): Item =
        item.withString(key, value)
    }

  implicit val stringSetMarshaller =
    new AttributeMarshaller[Set[String]] {
      def apply(item: Item, key: String, value: Set[String]): Item =
        item.withStringSet(key, value.asJava)
    }

  implicit val zonedDateTimeMarshaller =
    new AttributeMarshaller[ZonedDateTime] {
      def apply(item: Item, key: String, value: ZonedDateTime): Item =
        item.withString(key, value.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
    }

  implicit val instantMarshaller =
    new AttributeMarshaller[Instant] {
      def apply(item: Item, key: String, value: Instant): Item =
        item.withLong(key, value.toEpochMilli)
    }

  implicit def optionMarshaller[A](implicit marshaller: AttributeMarshaller[A]) =
    new AttributeMarshaller[Option[A]] {
      def apply(item: Item, key: String, value: Option[A]): Item =
        value match {
          case Some(v) =>
            marshaller(item, key, v)
          case None =>
            item.withNull(key)
        }
    }
}
