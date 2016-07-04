package scalamo.mapping

import scalamo.UnitSpec

class ItemUnmarshallerSpec extends UnitSpec {
  import TestData._
  import cats.data.Validated.{Invalid, Valid}
  import com.amazonaws.services.dynamodbv2.document.Item

  behavior of "ItemUnmarshaller"

  it should "return Invalid for a non-matching Item" in {
    val obj = ItemUnmarshaller[Class1].apply(new Item)
    obj shouldBe a[Invalid[_]]
  }

  it should "unmarshall supported types correctly" in {
    val obj = ItemUnmarshaller[Class1].apply(item)

    obj shouldBe a[Valid[_]]
    obj foreach { o =>
      o.number shouldEqual number
      o.bigInteger shouldEqual bigInt
      o.binary shouldEqual binary1
      o.boolean shouldBe boolean
      o.byteBuffer shouldEqual byteBuffer
      o.double shouldBe double
      o.float shouldBe float
      o.int shouldBe int
      o.long shouldBe long
      //o.intMap shouldEqual intMap
      o.stringMap shouldBe stringMap
      o.string shouldBe string
      o.zonedDateTime shouldBe zonedDateTime
      o.instant shouldBe instant
    }
  }

  it should "unmarshall optional types correctly" in {
    val obj = ItemUnmarshaller[Class2].apply(item)

    obj shouldBe a[Valid[_]]
    obj foreach { o =>
      o.number shouldEqual Some(number)
      o.bigInteger shouldBe Some(bigInt)
      o.binary shouldEqual Some(binary1)
      o.boolean shouldBe Some(boolean)
      o.byteBuffer shouldEqual Some(byteBuffer)
      o.double shouldBe Some(double)
      o.float shouldBe Some(float)
      o.int shouldBe Some(int)
      o.long shouldBe Some(long)
      //o.intMap shouldEqual Some(intMap)
      o.stringMap shouldBe Some(stringMap)
      o.string shouldBe Some(string)
      o.zonedDateTime shouldBe Some(zonedDateTime)
      o.instant shouldBe Some(instant)
    }
  }

  it should "handle missing optional values correctly" in {
    val obj = ItemUnmarshaller[Class2].apply(new Item())

    obj shouldBe a[Valid[_]]
    obj foreach { o =>
      o.number shouldBe None
      o.bigInteger shouldBe None
      o.binary shouldBe None
      o.boolean shouldBe None
      o.byteBuffer shouldBe None
      o.double shouldBe None
      o.float shouldBe None
      o.int shouldBe None
      o.long shouldBe None
      //o.intMap shouldBe None
      o.stringMap shouldBe None
      o.string shouldBe None
      o.zonedDateTime shouldBe None
      o.instant shouldBe None
    }
  }
}
