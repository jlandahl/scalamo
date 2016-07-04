package scalamo.mapping

import scalamo.UnitSpec

class AttributeUnmarshallerSpec extends UnitSpec {
  import TestData._
  import java.nio.ByteBuffer
  import java.time.{Instant, ZonedDateTime}

  behavior of "AttributeUnmarshaller"

  it should "unmarshall BigDecimal" in {
    AttributeUnmarshaller[BigDecimal].apply(item, "number") shouldEqual number
  }

  it should "unmarshall BigInt" in {
    AttributeUnmarshaller[BigInt].apply(item, "bigInteger") shouldEqual bigInt
  }

  it should "unmarshall Array[Byte]" in {
    AttributeUnmarshaller[Array[Byte]].apply(item, "binary") shouldEqual binary1
  }

  it should "unmarshall Boolean" in {
    AttributeUnmarshaller[Boolean].apply(item, "boolean") shouldBe true
  }

  it should "unmarshall ByteBuffer" in {
    AttributeUnmarshaller[ByteBuffer].apply(item, "byteBuffer") shouldEqual byteBuffer
  }

  it should "unmarshall Set[ByteBuffer]" in {
    AttributeUnmarshaller[Set[ByteBuffer]].apply(item, "binarySet") shouldEqual byteBufferSet
  }

  it should "unmarshall Double" in {
    AttributeUnmarshaller[Double].apply(item, "double") shouldBe double
  }

  it should "unmarshall Float" in {
    AttributeUnmarshaller[Float].apply(item, "float") shouldBe float
  }

  it should "unmarshall Int" in {
    AttributeUnmarshaller[Int].apply(item, "int") shouldBe int
  }

  it should "unmarshall List[Int]" in {
    //AttributeUnmarshaller[List[Int]].apply(item, "intList") shouldEqual intList
  }

  it should "unmarshall List[String]" in {
    AttributeUnmarshaller[List[String]].apply(item, "stringList") shouldEqual stringList
  }

  it should "unmarshall Long" in {
    AttributeUnmarshaller[Long].apply(item, "long") shouldEqual long
  }

  it should "unmarshall Map[String, Int]" in {
    //AttributeUnmarshaller[Map[String, Int]].apply(item, "intMap") shouldEqual intMap
  }

  it should "unmarshall Map[String, String]" in {
    AttributeUnmarshaller[Map[String, String]].apply(item, "stringMap") shouldEqual stringMap
  }

  it should "unmarshall String" in {
    AttributeUnmarshaller[String].apply(item, "string") shouldBe "string"
  }

  it should "unmarshall ZonedDateTime" in {
    AttributeUnmarshaller[ZonedDateTime].apply(item, "zonedDateTime") shouldBe zonedDateTime
  }

  it should "unmarshall Instant" in {
    AttributeUnmarshaller[Instant].apply(item, "instant") shouldBe instant
  }
}
