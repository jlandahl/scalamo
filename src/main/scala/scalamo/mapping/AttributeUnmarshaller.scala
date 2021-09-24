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

  implicit val bigDecimalUnmarshaller: AttributeUnmarshaller[BigDecimal] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as BigDecimal")

  implicit val bigIntUnmarshaller: AttributeUnmarshaller[BigInt] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as BigInt")

  implicit val binaryUnmarshaller: AttributeUnmarshaller[Array[Byte]] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Array[Byte]")

  implicit val booleanUnmarshaller: AttributeUnmarshaller[Boolean] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Boolean")

  implicit val byteBufferUnmarshaller: AttributeUnmarshaller[ByteBuffer] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as ByteBuffer")

  implicit val byteBufferSetUnmarshaller: AttributeUnmarshaller[Set[ByteBuffer]] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Set[ByteBuffer]")

  implicit val doubleUnmarshaller: AttributeUnmarshaller[Double] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Double")

  implicit val floatUnmarshaller: AttributeUnmarshaller[Float] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Float")

  implicit val intUnmarshaller: AttributeUnmarshaller[Int] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Int")

  implicit val longUnmarshaller: AttributeUnmarshaller[Long] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Long")

  implicit def mapUnmarshaller[A]: AttributeUnmarshaller[Map[String, A]] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Map[String, A]")

  implicit val stringUnmarshaller: AttributeUnmarshaller[String] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as String")

  implicit val zonedDateTimeUnmarshaller: AttributeUnmarshaller[ZonedDateTime] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as ZonedDateTime")

  implicit val instantUnmarshaller: AttributeUnmarshaller[Instant] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Instant")

  implicit def seqUnmarshaller[A]: AttributeUnmarshaller[Seq[A]] =
    (item: Item, attr: String) =>
      nonOptional(item, attr, s"Error getting '$attr' as Seq")

  implicit def optionUnmarshaller[A](implicit extractor: AttributeExtractor[A]): AttributeUnmarshaller[Option[A]] =
    (item: Item, attr: String) =>
      optional(item, attr, s"Error getting '$attr' as ???")

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
  implicit def listUnmarshaller[A](implicit extractor: AttributeExtractor[List[A]]): AttributeUnmarshaller[List[A]] =
    (item: Item, attr: String) =>
      Validated.catchNonFatal(extractor(item, attr), s"Error getting '$attr' as List[A]")
}
