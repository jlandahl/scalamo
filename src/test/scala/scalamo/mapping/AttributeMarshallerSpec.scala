package scalamo.mapping

import scalamo.UnitSpec

class AttributeMarshallerSpec extends UnitSpec {
  import TestData._
  import com.amazonaws.services.dynamodbv2.document.Item
  import java.nio.ByteBuffer
  import java.time.format.DateTimeFormatter
  import java.time.{Instant, ZonedDateTime}
  import scala.collection.JavaConverters._

  behavior of "AttributeMarshaller"

  it should "marshall BigDecimal" in {
    val item = AttributeMarshaller[BigDecimal].apply(new Item, "number", number)
    BigDecimal(item.getNumber("number")) shouldBe number
  }

  it should "marshall BigInt" in {
    val item = AttributeMarshaller[BigInt].apply(new Item, "bigInteger", bigInt)
    BigInt(item.getBigInteger("bigInteger")) shouldBe bigInt
  }

  it should "marshall Array[Byte]" in {
    val item = AttributeMarshaller[Array[Byte]].apply(new Item, "binary", binary1)
    item.getBinary("binary") shouldEqual binary1
  }

  it should "marshall Boolean" in {
    val item = AttributeMarshaller[Boolean].apply(new Item, "boolean", boolean)
    item.getBoolean("boolean") shouldBe true
  }

  it should "marshall ByteBuffer" in {
    val item = AttributeMarshaller[ByteBuffer].apply(new Item, "byteBuffer", byteBuffer)
    item.getByteBuffer("byteBuffer") shouldBe byteBuffer
  }

  it should "marshall Set[ByteBuffer]" in {
    val item = AttributeMarshaller[Set[ByteBuffer]].apply(new Item, "byteBufferSet", byteBufferSet)
    item.getByteBufferSet("byteBufferSet").asScala shouldBe byteBufferSet
  }

  it should "marshall Double" in {
    val item = AttributeMarshaller[Double].apply(new Item, "double", double)
    item.getDouble("double") shouldBe double
  }

  it should "marshall Float" in {
    val item = AttributeMarshaller[Float].apply(new Item, "float", float)
    item.getFloat("float") shouldBe float
  }

  it should "marshall Int" in {
    val item = AttributeMarshaller[Int].apply(new Item, "int", int)
    item.getInt("int") shouldBe int
  }

  it should "marshall List[Int]" in {
    //val item = AttributeMarshaller[List[Int]].apply(new Item, "intList", intList)
    //item.getList[Int]("intList") shouldEqual intList
  }

  it should "marshall List[String]" in {
    val item = AttributeMarshaller[List[String]].apply(new Item, "stringList", stringList)
    item.getList[String]("stringList").asScala shouldEqual stringList
  }

  it should "marshall Long" in {
    val item = AttributeMarshaller[Long].apply(new Item, "long", long)
    item.getLong("long") shouldEqual long
  }

  it should "marshall Map[String, Int]" in {
    //val item = AttributeMarshaller[Map[String, Int]].apply(new Item, "intMap", intMap)
    //item.getMap ... shouldEqual intMap
  }

  it should "marshall Map[String, String]" in {
    val item = AttributeMarshaller[Map[String, String]].apply(new Item, "stringMap", stringMap)
    item.getMap[String]("stringMap").asScala shouldEqual stringMap
  }

  it should "marshall String" in {
    val item = AttributeMarshaller[String].apply(new Item, "string", string)
    item.getString("string") shouldBe "string"
  }

  it should "marshall ZonedDateTime" in {
    val item = AttributeMarshaller[ZonedDateTime].apply(new Item, "zonedDateTime", zonedDateTime)
    item.getString("zonedDateTime") shouldEqual zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
  }

  it should "marshall Instant" in {
    val item = AttributeMarshaller[Instant].apply(new Item, "instant", instant)
    item.getLong("instant") shouldBe instant.toEpochMilli
  }
}
