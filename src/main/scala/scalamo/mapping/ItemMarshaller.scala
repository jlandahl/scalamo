package scalamo.mapping

import com.amazonaws.services.dynamodbv2.document.Item

trait ItemMarshaller[A] {
  def apply(a: A): Item
}

object ItemMarshaller extends ItemMarshallerFunctions with ItemMarshallers

trait ItemMarshallerFunctions {
  def apply[A](implicit marshaller: ItemMarshaller[A]): ItemMarshaller[A] =
    marshaller
}

trait ItemMarshallers {
  import shapeless._
  import shapeless.labelled.FieldType

  implicit val hnilItemMarshaller: ItemMarshaller[HNil] =
    (a: HNil) => new Item

  implicit def hconsItemMarshaller[K <: Symbol, V, T <: HList](implicit
                                                               witness: Witness.Aux[K],
                                                               itemMarshallerT: Lazy[ItemMarshaller[T]],
                                                               attributeMarshaller: Lazy[AttributeMarshaller[V]]
                                                              ): ItemMarshaller[FieldType[K, V] :: T] =
    (a: FieldType[K, V] :: T) => {
      val item = itemMarshallerT.value(a.tail)
      attributeMarshaller.value(item, witness.value.name, a.head)
    }

  implicit def caseClassMarshaller[A, R <: HList](implicit
                                                  gen: LabelledGeneric.Aux[A, R],
                                                  marshaller: Lazy[ItemMarshaller[R]]
                                                 ): ItemMarshaller[A] =
    (a: A) => marshaller.value(gen.to(a))
}
