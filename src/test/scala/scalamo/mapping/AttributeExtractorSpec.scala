package scalamo.mapping

class AttributeExtractorSpec extends scalamo.UnitSpec {
  import TestData._
  import java.nio.ByteBuffer
  import java.time.{Instant, ZonedDateTime}

  behavior of "AttributeExtractor"

  it should "extract BigDecimal" in {
    AttributeExtractor[BigDecimal].apply(item, "number") shouldEqual number
  }

  it should "extract BigInt" in {
    AttributeExtractor[BigInt].apply(item, "bigInteger") shouldEqual bigInt
  }

  it should "extract Array[Byte]" in {
    AttributeExtractor[Array[Byte]].apply(item, "binary") shouldEqual binary1
  }

  it should "extract Boolean" in {
    AttributeExtractor[Boolean].apply(item, "boolean") shouldBe true
  }

  it should "extract ByteBuffer" in {
    AttributeExtractor[ByteBuffer].apply(item, "byteBuffer") shouldEqual byteBuffer
  }

  it should "extract Set[ByteBuffer]" in {
    AttributeExtractor[Set[ByteBuffer]].apply(item, "binarySet") shouldEqual byteBufferSet
  }

  it should "extract Double" in {
    AttributeExtractor[Double].apply(item, "double") shouldBe double
  }

  it should "extract Float" in {
    AttributeExtractor[Float].apply(item, "float") shouldBe float
  }

  it should "extract Int" in {
    AttributeExtractor[Int].apply(item, "int") shouldBe int
  }

  it should "extract List[Int]" in {
    //AttributeExtractor[List[Int]].apply(item, "intList") shouldEqual intList
  }

  it should "extract List[String]" in {
    AttributeExtractor[List[String]].apply(item, "stringList") shouldEqual stringList
  }

  it should "extract Long" in {
    AttributeExtractor[Long].apply(item, "long") shouldEqual long
  }

  it should "extract Map[String, Int]" in {
    //AttributeExtractor[Map[String, Int]].apply(item, "intMap") shouldEqual intMap
  }

  it should "extract Map[String, String]" in {
    AttributeExtractor[Map[String, String]].apply(item, "stringMap") shouldEqual stringMap
  }

  it should "extract String" in {
    AttributeExtractor[String].apply(item, "string") shouldBe "string"
  }

  it should "extract ZonedDateTime" in {
    AttributeExtractor[ZonedDateTime].apply(item, "zonedDateTime") shouldBe zonedDateTime
  }

  it should "extract Instant" in {
    AttributeExtractor[Instant].apply(item, "instant") shouldBe instant
  }
}
