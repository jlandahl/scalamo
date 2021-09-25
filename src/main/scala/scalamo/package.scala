package object scalamo {
  import cats.Traverse
  import cats.data.ValidatedNel
  import com.amazonaws.services.dynamodbv2.document.{BatchWriteItemOutcome, DynamoDB, Item, ItemCollection, PutItemOutcome, ScanOutcome, Table, TableKeysAndAttributes, TableWriteItems}
  import scala.jdk.CollectionConverters._
  import scalamo.mapping.{AttributeUnmarshaller, ItemMarshaller, ItemUnmarshaller}

  type Validated[A] = ValidatedNel[Throwable, A]

  object Validated {
    def catchNonFatal[A](f: => A): Validated[A] =
      cats.data.Validated.catchNonFatal(f).toValidatedNel

    def catchNonFatal[A](f: => A, message: String): Validated[A] =
      cats.data.Validated.catchNonFatal(f).leftMap(t => new Exception(message, t)).toValidatedNel
  }

  implicit class ItemOps(val item: Item) extends AnyVal {
    def hasValue(attr: String): Boolean =
      item.isPresent(attr) && !item.isNull(attr)

    def getAs[A](attr: String)(implicit unmarshaller: AttributeUnmarshaller[A]): Validated[A] =
      unmarshaller(item, attr)

    def as[A](implicit unmarshaller: ItemUnmarshaller[A]): Validated[A] =
      unmarshaller(item)
  }

  implicit class TableOps(val table: Table) extends AnyVal {
    import com.amazonaws.services.dynamodbv2.document.{KeyAttribute, PrimaryKey}

    def get[A, B](hashKeyName: String, hashKeyValue: B)(implicit unmarshaller: ItemUnmarshaller[A]): Validated[A] =
      unmarshaller(table.getItem(hashKeyName, hashKeyValue))

    def get[A](primaryKeyComponents: KeyAttribute*)(implicit unmarshaller: ItemUnmarshaller[A]): Validated[A] =
      unmarshaller(table.getItem(primaryKeyComponents: _*))

    def get[A](primaryKey: PrimaryKey)(implicit unmarshaller: ItemUnmarshaller[A]): Validated[A] =
      unmarshaller(table.getItem(primaryKey))

    def scan(filterExpression: String,
             nameMap: Map[String, String],
             valueMap: Map[String, AnyRef]): ItemCollection[ScanOutcome] =
      table.scan(filterExpression, nameMap.asJava, valueMap.asJava)

    def scan(filterExpression: String,
             projectionExpression: String,
             nameMap: Map[String, String],
             valueMap: Map[String, AnyRef]): ItemCollection[ScanOutcome] =
      table.scan(filterExpression, projectionExpression, nameMap.asJava, valueMap.asJava)

    def put[A](a: A)(implicit marshaller: ItemMarshaller[A]): PutItemOutcome =
      table.putItem(marshaller(a))
  }

  implicit class ItemCollectionOps[A](val itemCollection: ItemCollection[A]) extends AnyVal {
    def iterateAs[B](implicit unmarshaller: ItemUnmarshaller[B]): Iterator[Validated[B]] =
      itemCollection.iterator().asScala.map(unmarshaller.apply)
  }

  implicit class DynamoDBOps(val dynamoDB: DynamoDB) extends AnyVal {
    def get[A, B](table: String, hashKeyName: String, hashKeyValues: B*)(implicit unmarshaller: ItemUnmarshaller[A]): Validated[Seq[A]] = {
      val tka = new TableKeysAndAttributes(table).addHashOnlyPrimaryKeys(hashKeyName, hashKeyValues: _*)
      val results = dynamoDB.batchGetItem(tka).getTableItems.get(table).asScala.map(unmarshaller(_)).toSeq
      Traverse[Seq].sequence(results)
    }

    def put[A](table: String, as: Iterable[A])(implicit marshaller: ItemMarshaller[A]): BatchWriteItemOutcome =
      dynamoDB.batchWriteItem(new TableWriteItems(table).withItemsToPut(as.map(marshaller(_)).asJavaCollection))
  }
}
