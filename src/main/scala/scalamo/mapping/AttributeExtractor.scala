package scalamo.mapping

import com.amazonaws.services.dynamodbv2.document.Item

trait AttributeExtractor[T] {
  def apply(item: Item, attr: String): T
}

object AttributeExtractor
  extends AttributeExtractorFunctions
    with AttributeExtractors
    with AttributeExtractors1

trait AttributeExtractorFunctions {
  def apply[A](implicit extractor: AttributeExtractor[A]): AttributeExtractor[A] =
    extractor
}

trait AttributeExtractors {
  import java.nio.ByteBuffer
  import java.time.format.DateTimeFormatter
  import java.time.{Instant, ZonedDateTime}
  import scala.jdk.CollectionConverters._

  implicit val bigDecimalExtractor: AttributeExtractor[BigDecimal] =
    (item: Item, attr: String) => BigDecimal(item.getNumber(attr))

  implicit val bigIntExtractor: AttributeExtractor[BigInt] =
    (item: Item, attr: String) => BigInt(item.getBigInteger(attr))

  implicit val binaryExtractor: AttributeExtractor[Array[Byte]] =
    (item: Item, attr: String) => item.getBinary(attr)

  implicit val booleanExtractor: AttributeExtractor[Boolean] =
    (item: Item, attr: String) => item.getBoolean(attr)

  implicit val byteBufferExtractor: AttributeExtractor[ByteBuffer] =
    (item: Item, attr: String) => item.getByteBuffer(attr)

  implicit val byteBufferSetExtractor: AttributeExtractor[Set[ByteBuffer]] =
    (item: Item, attr: String) => item.getByteBufferSet(attr).asScala.toSet

  implicit val doubleExtractor: AttributeExtractor[Double] =
    (item: Item, attr: String) => item.getDouble(attr)

  implicit val floatExtractor: AttributeExtractor[Float] =
    (item: Item, attr: String) => item.getFloat(attr)

  implicit val intExtractor: AttributeExtractor[Int] =
    (item: Item, attr: String) => item.getInt(attr)

  implicit val longExtractor: AttributeExtractor[Long] =
    (item: Item, attr: String) => item.getLong(attr)

  implicit def mapExtractor[A]: AttributeExtractor[Map[String, A]] =
    (item: Item, attr: String) => item.getMap[A](attr).asScala.toMap

  implicit val stringExtractor: AttributeExtractor[String] =
    (item: Item, attr: String) => item.getString(attr)

  implicit val zonedDateTimeExtractor: AttributeExtractor[ZonedDateTime] =
    (item: Item, attr: String) =>
      ZonedDateTime.parse(stringExtractor(item, attr), DateTimeFormatter.ISO_ZONED_DATE_TIME)

  implicit val instantExtractor: AttributeExtractor[Instant] =
    (item: Item, attr: String) => Instant.ofEpochMilli(longExtractor(item, attr))

  implicit def seqExtractor[A]: AttributeExtractor[Seq[A]] =
    (item: Item, attr: String) => item.getList[A](attr).asScala.toSeq
}

trait AttributeExtractors1 {
  implicit def listExtractor[A]: AttributeExtractor[List[A]] =
    (item: Item, attr: String) => AttributeExtractor.seqExtractor(item, attr).toList
}
