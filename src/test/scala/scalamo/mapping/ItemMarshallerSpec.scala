package scalamo.mapping

import scalamo.UnitSpec

class ItemMarshallerSpec extends UnitSpec {
  import TestData._
  import java.time.format.DateTimeFormatter
  import scala.collection.JavaConverters._

  behavior of "ItemMarshaller"

  it should "marshall supported types correctly" in {
    val item = ItemMarshaller[Class1].apply(obj1)

    BigDecimal(item.getNumber("number")) shouldBe number
    BigInt(item.getBigInteger("bigInteger")) shouldBe bigInt
    item.getBinary("binary") shouldBe binary1
    item.getBoolean("boolean") shouldBe boolean
    item.getByteBuffer("byteBuffer") shouldBe byteBuffer
    item.getByteBufferSet("byteBufferSet").asScala shouldBe byteBufferSet
    item.getDouble("double") shouldBe double
    item.getFloat("float") shouldBe float
    item.getInt("int") shouldBe int
    item.getLong("long") shouldBe long
    //item.getMap[Int]("intMap").asScala shouldBe intMap
    item.getMap[String]("stringMap").asScala shouldBe stringMap
    item.getString("string") shouldBe string
    item.getString("zonedDateTime") shouldBe zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    item.getLong("instant") shouldBe instant.toEpochMilli
  }

  it should "marshall Some values correctly" in {
    val item = ItemMarshaller[Class2].apply(obj2)

    BigDecimal(item.getNumber("number")) shouldBe number
    BigInt(item.getBigInteger("bigInteger")) shouldBe bigInt
    item.getBinary("binary") shouldBe binary1
    item.getBoolean("boolean") shouldBe boolean
    item.getByteBuffer("byteBuffer") shouldBe byteBuffer
    item.getByteBufferSet("byteBufferSet").asScala shouldBe byteBufferSet
    item.getDouble("double") shouldBe double
    item.getFloat("float") shouldBe float
    item.getInt("int") shouldBe int
    item.getLong("long") shouldBe long
    //item.getMap[Int]("intMap").asScala shouldBe intMap
    item.getMap[String]("stringMap").asScala shouldBe stringMap
    item.getString("string") shouldBe string
    item.getString("zonedDateTime") shouldBe zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    item.getLong("instant") shouldBe instant.toEpochMilli
  }

  it should "marshall None values correctly" in {
    val item = ItemMarshaller[Class2].apply(obj3)

    item.isNull("number") shouldBe true
    item.isNull("bigInteger") shouldBe true
    item.isNull("binary") shouldBe true
    item.isNull("boolean") shouldBe true
    item.isNull("byteBuffer") shouldBe true
    item.isNull("byteBufferSet") shouldBe true
    item.isNull("double") shouldBe true
    item.isNull("float") shouldBe true
    item.isNull("int") shouldBe true
    item.isNull("long") shouldBe true
    //item.isNull("intMap") shouldBe true
    item.isNull("stringMap") shouldBe true
    item.isNull("string") shouldBe true
    item.isNull("zonedDateTime") shouldBe true
    item.isNull("instant") shouldBe true
  }
}
