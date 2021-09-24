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

  implicit val bigDecimalExtractor = new AttributeExtractor[BigDecimal] {
    def apply(item: Item, attr: String): BigDecimal =
      BigDecimal(item.getNumber(attr))
  }

  implicit val bigIntExtractor = new AttributeExtractor[BigInt] {
    def apply(item: Item, attr: String): BigInt =
      BigInt(item.getBigInteger(attr))
  }

  implicit val binaryExtractor = new AttributeExtractor[Array[Byte]] {
    def apply(item: Item, attr: String): Array[Byte] =
      item.getBinary(attr)
  }

  implicit val booleanExtractor = new AttributeExtractor[Boolean] {
    def apply(item: Item, attr: String): Boolean = item.getBoolean(attr)
  }

  implicit val byteBufferExtractor = new AttributeExtractor[ByteBuffer] {
    def apply(item: Item, attr: String): ByteBuffer = item.getByteBuffer(attr)
  }

  implicit val byteBufferSetExtractor = new AttributeExtractor[Set[ByteBuffer]] {
    def apply(item: Item, attr: String): Set[ByteBuffer] =
      item.getByteBufferSet(attr).asScala.toSet
  }

  implicit val doubleExtractor = new AttributeExtractor[Double] {
    def apply(item: Item, attr: String): Double = item.getDouble(attr)
  }

  implicit val floatExtractor = new AttributeExtractor[Float] {
    def apply(item: Item, attr: String): Float = item.getFloat(attr)
  }

  implicit val intExtractor = new AttributeExtractor[Int] {
    def apply(item: Item, attr: String): Int = item.getInt(attr)
  }

  implicit val longExtractor = new AttributeExtractor[Long] {
    def apply(item: Item, attr: String): Long = item.getLong(attr)
  }

  implicit def mapExtractor[A] = new AttributeExtractor[Map[String, A]] {
    def apply(item: Item, attr: String): Map[String, A] = item.getMap[A](attr).asScala.toMap
  }

  implicit val stringExtractor = new AttributeExtractor[String] {
    def apply(item: Item, attr: String): String = item.getString(attr)
  }
  implicit val zonedDateTimeExtractor = new AttributeExtractor[ZonedDateTime] {
    def apply(item: Item, attr: String): ZonedDateTime =
      ZonedDateTime.parse(stringExtractor(item, attr), DateTimeFormatter.ISO_ZONED_DATE_TIME)
  }
  implicit val instantExtractor = new AttributeExtractor[Instant] {
    def apply(item: Item, attr: String): Instant =
      Instant.ofEpochMilli(longExtractor(item, attr))
  }

  implicit def seqExtractor[A] = new AttributeExtractor[Seq[A]] {
    def apply(item: Item, attr: String): Seq[A] = item.getList[A](attr).asScala.toSeq
  }
}

trait AttributeExtractors1 {
  implicit def listExtractor[A] = new AttributeExtractor[List[A]] {
    def apply(item: Item, attr: String): List[A] =
      AttributeExtractor.seqExtractor(item, attr).toList
  }
}
