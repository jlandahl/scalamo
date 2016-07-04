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
    new ItemMarshaller[HNil] {
      override def apply(a: HNil): Item = new Item
    }

  implicit def hconsItemMarshaller[K <: Symbol, V, T <: HList](implicit
                                                               witness: Witness.Aux[K],
                                                               itemMarshallerT: Lazy[ItemMarshaller[T]],
                                                               attributeMarshaller: Lazy[AttributeMarshaller[V]]
                                                              ): ItemMarshaller[FieldType[K, V] :: T] =
    new ItemMarshaller[FieldType[K, V] :: T] {
      override def apply(a: FieldType[K, V] :: T): Item = {
        val item = itemMarshallerT.value(a.tail)
        attributeMarshaller.value(item, witness.value.name, a.head)
      }
    }

  implicit def caseClassMarshaller[A, R <: HList](implicit
                                                  gen: LabelledGeneric.Aux[A, R],
                                                  marshaller: Lazy[ItemMarshaller[R]]
                                                 ): ItemMarshaller[A] =
    new ItemMarshaller[A] {
      def apply(a: A): Item =
        marshaller.value(gen.to(a))
    }
}
