package scalamo.mapping

object TestData {
  import com.amazonaws.services.dynamodbv2.document.Item
  import java.time.{Instant, ZonedDateTime}
  import java.nio.ByteBuffer
  import scala.collection.JavaConverters._

  case class Class1(number: BigDecimal,
                    bigInteger: BigInt,
                    binary: Array[Byte],
                    boolean: Boolean,
                    byteBuffer: ByteBuffer,
                    byteBufferSet: Set[ByteBuffer],
                    double: Double,
                    float: Float,
                    int: Int,
                    long: Long,
                    intMap: Map[String, Int],
                    stringMap: Map[String, String],
                    string: String,
                    zonedDateTime: ZonedDateTime,
                    instant: Instant)

  case class Class2(number: Option[BigDecimal],
                    bigInteger: Option[BigInt],
                    binary: Option[Array[Byte]],
                    boolean: Option[Boolean],
                    byteBuffer: Option[ByteBuffer],
                    byteBufferSet: Option[Set[ByteBuffer]],
                    double: Option[Double],
                    float: Option[Float],
                    int: Option[Int],
                    long: Option[Long],
                    intMap: Option[Map[String, Int]],
                    stringMap: Option[Map[String, String]],
                    string: Option[String],
                    zonedDateTime: Option[ZonedDateTime],
                    instant: Option[Instant])

  val bigDecimalSet = Set(BigDecimal(1).underlying, BigDecimal(2).underlying).asJava
  val bigInt = BigInt(1)
  val binary1 = Array[Byte](1, 2)
  val binary2 = Array[Byte](3, 4)
  val binarySet = Set(binary1, binary2)
  val boolean = true
  val byteBuffer = ByteBuffer.wrap(binary1)
  val byteBufferSet = Set(ByteBuffer.wrap(binary1), ByteBuffer.wrap(binary2))
  val double = 1.1
  val float = 1.1f
  val int = 1
  val intList = List(1, 2, 3)
  val doubleList = List(0.1, 0.2, 0.3)
  val stringList = List("a", "b", "c")
  val long = 1L
  val intMap = Map("a" -> 1)
  val string = "string"
  val stringMap = Map("a" -> "b")
  val number = BigDecimal(1)
  val numberSet = bigDecimalSet.asInstanceOf[java.util.Set[Number]]
  val zonedDateTime = ZonedDateTime.parse("1970-01-01T00:00:00Z")
  val instant = Instant.ofEpochMilli(0)

  val item =
    new Item()
      .withBigDecimalSet("bigDecimalSet", bigDecimalSet)
      .withBigInteger("bigInteger", bigInt.underlying)
      .withBinary("binary", binary1)
      .withBinarySet("binarySet", binarySet.asJava)
      .withBoolean("boolean", boolean)
      .withBinary("byteBuffer", byteBuffer)
      .withByteBufferSet("byteBufferSet", byteBufferSet.asJava)
      .withDouble("double", double)
      .withFloat("float", float)
      .withInt("int", int)
      .withList("intList", intList.asJava)
      .withList("doubleList", doubleList.asJava)
      .withList("stringList", stringList.asJava)
      .withLong("long", long)
      .withMap("intMap", intMap.asJava)
      .withMap("stringMap", stringMap.asJava)
      .withNumber("number", number.underlying)
      .withNumberSet("numberSet", numberSet)
      .withShort("short", Short.MinValue)
      .withString("string", string)
      .withString("zonedDateTime", zonedDateTime.toString)
      .withLong("instant", instant.toEpochMilli)

  val obj1 = Class1(number = number,
                    bigInteger = bigInt,
                    binary = binary1,
                    boolean = boolean,
                    byteBuffer = byteBuffer,
                    byteBufferSet = byteBufferSet,
                    double = double,
                    float = float,
                    int = int,
                    long = long,
                    intMap = intMap,
                    stringMap = stringMap,
                    string = string,
                    zonedDateTime = zonedDateTime,
                    instant = instant)

  val obj2 = Class2(number = Some(number),
                    bigInteger = Some(bigInt),
                    binary = Some(binary1),
                    boolean = Some(boolean),
                    byteBuffer = Some(byteBuffer),
                    byteBufferSet = Some(byteBufferSet),
                    double = Some(double),
                    float = Some(float),
                    int = Some(int),
                    long = Some(long),
                    intMap = Some(intMap),
                    stringMap = Some(stringMap),
                    string = Some(string),
                    zonedDateTime = Some(zonedDateTime),
                    instant = Some(instant))

  val obj3 = Class2(number = None,
                    bigInteger = None,
                    binary = None,
                    boolean = None,
                    byteBuffer = None,
                    byteBufferSet = None,
                    double = None,
                    float = None,
                    int = None,
                    long = None,
                    intMap = None,
                    stringMap = None,
                    string = None,
                    zonedDateTime = None,
                    instant = None)
}
