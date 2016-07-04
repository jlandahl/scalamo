package scalamo.mapping

import com.amazonaws.services.dynamodbv2.document.Item
import scalamo.Validated

trait AttributeUnmarshaller[A] {
  def apply(item: Item, attr: String): Validated[A]
}

object AttributeUnmarshaller
  extends AttributeExtractorFunctions
    with AttributeUnmarshallers
    with AttributeUnmarshallers1

trait AttributeUnmarshallerFunctions {
  def apply[A](implicit unmarshaller: AttributeUnmarshaller[A]): AttributeUnmarshaller[A] =
    unmarshaller
}

trait AttributeUnmarshallers {
  import java.nio.ByteBuffer
  import java.time.{Instant, ZonedDateTime}
  import cats.syntax.validated._

  implicit val bigDecimalUnmarshaller = new AttributeUnmarshaller[BigDecimal] {
    def apply(item: Item, attr: String): Validated[BigDecimal] =
      nonOptional(item, attr, s"Error getting '$attr' as BigDecimal")
  }

  implicit val bigIntUnmarshaller = new AttributeUnmarshaller[BigInt] {
    def apply(item: Item, attr: String): Validated[BigInt] =
      nonOptional(item, attr, s"Error getting '$attr' as BigInt")
  }

  implicit val binaryUnmarshaller = new AttributeUnmarshaller[Array[Byte]] {
    def apply(item: Item, attr: String): Validated[Array[Byte]] =
      nonOptional(item, attr, s"Error getting '$attr' as Array[Byte]")
  }

  implicit val booleanUnmarshaller = new AttributeUnmarshaller[Boolean] {
    def apply(item: Item, attr: String): Validated[Boolean] =
      nonOptional(item, attr, s"Error getting '$attr' as Boolean")
  }

  implicit val byteBufferUnmarshaller = new AttributeUnmarshaller[ByteBuffer] {
    def apply(item: Item, attr: String): Validated[ByteBuffer] =
      nonOptional(item, attr, s"Error getting '$attr' as ByteBuffer")
  }

  implicit val byteBufferSetUnmarshaller = new AttributeUnmarshaller[Set[ByteBuffer]] {
    def apply(item: Item, attr: String): Validated[Set[ByteBuffer]] =
      nonOptional(item, attr, s"Error getting '$attr' as Set[ByteBuffer]")
  }

  implicit val doubleUnmarshaller = new AttributeUnmarshaller[Double] {
    def apply(item: Item, attr: String): Validated[Double] =
      nonOptional(item, attr, s"Error getting '$attr' as Double")
  }

  implicit val floatUnmarshaller = new AttributeUnmarshaller[Float] {
    def apply(item: Item, attr: String): Validated[Float] =
      nonOptional(item, attr, s"Error getting '$attr' as Float")
  }

  implicit val intUnmarshaller = new AttributeUnmarshaller[Int] {
    def apply(item: Item, attr: String): Validated[Int] =
      nonOptional(item, attr, s"Error getting '$attr' as Int")
  }

  implicit val longUnmarshaller = new AttributeUnmarshaller[Long] {
    def apply(item: Item, attr: String): Validated[Long] =
      nonOptional(item, attr, s"Error getting '$attr' as Long")
  }

  implicit def mapUnmarshaller[A] = new AttributeUnmarshaller[Map[String, A]] {
    def apply(item: Item, attr: String): Validated[Map[String, A]] =
      nonOptional(item, attr, s"Error getting '$attr' as Map[String, A]")
  }

  implicit val stringUnmarshaller = new AttributeUnmarshaller[String] {
    def apply(item: Item, attr: String): Validated[String] =
      nonOptional(item, attr, s"Error getting '$attr' as String")
  }

  implicit val zonedDateTimeUnmarshaller = new AttributeUnmarshaller[ZonedDateTime] {
    def apply(item: Item, attr: String): Validated[ZonedDateTime] =
      nonOptional(item, attr, s"Error getting '$attr' as ZonedDateTime")
  }

  implicit val instantUnmarshaller = new AttributeUnmarshaller[Instant] {
    def apply(item: Item, attr: String): Validated[Instant] =
      nonOptional(item, attr, s"Error getting '$attr' as Instant")
  }

  implicit def seqUnmarshaller[A] = new AttributeUnmarshaller[Seq[A]] {
    def apply(item: Item, attr: String): Validated[Seq[A]] =
      nonOptional(item, attr, s"Error getting '$attr' as Seq")
  }

  implicit def optionUnmarshaller[A](implicit extractor: AttributeExtractor[A]) = new AttributeUnmarshaller[Option[A]] {
    def apply(item: Item, attr: String): Validated[Option[A]] =
      optional(item, attr, s"Error getting '$attr' as ???")
  }

  def nonOptional[A](item: Item, attr: String, message: String)(implicit extractor: AttributeExtractor[A]): Validated[A] =
    Validated.catchNonFatal(extractor(item, attr), message)

  def optional[A](item: Item, attr: String, message: String)(implicit extractor: AttributeExtractor[A]): Validated[Option[A]] =
    if (item.hasAttribute(attr))
      try {
        Some(extractor(item, attr)).validNel
      } catch {
        case scala.util.control.NonFatal(t) =>
          (new Exception(message, t): Throwable).invalidNel
      }
    else
      None.validNel
}

trait AttributeUnmarshallers1 {
  implicit def listUnmarshaller[A](implicit extractor: AttributeExtractor[List[A]]) = new AttributeUnmarshaller[List[A]] {
    def apply(item: Item, attr: String): Validated[List[A]] =
      Validated.catchNonFatal(extractor(item, attr), s"Error getting '$attr' as List[A]")
  }
}
