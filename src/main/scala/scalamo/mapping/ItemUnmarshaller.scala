package scalamo.mapping

import cats.syntax.validated._
import com.amazonaws.services.dynamodbv2.document.Item
import scalamo.Validated

trait ItemUnmarshaller[A] {
  def apply(item: Item): Validated[A]
}

object ItemUnmarshaller extends ItemUnmarshallerFunctions with ItemUnmarshallers

trait ItemUnmarshallerFunctions {
  def apply[A](implicit unmarshaller: ItemUnmarshaller[A]): ItemUnmarshaller[A] =
    unmarshaller
}

trait ItemUnmarshallers {
  import cats.Apply
  import shapeless._
  import shapeless.labelled.{FieldType, field}

  implicit val hnilItemUnmarshaller: ItemUnmarshaller[HNil] =
    (item: Item) => HNil.valid

  implicit def hconsItemUnmarshaller[K <: Symbol, V, T <: HList](implicit
                                                                 witness: Witness.Aux[K],
                                                                 attributeUmarshaller: Lazy[AttributeUnmarshaller[V]],
                                                                 itemUnmarshallerT: Lazy[ItemUnmarshaller[T]]
                                                                ): ItemUnmarshaller[FieldType[K, V] :: T] =
    (item: Item) => {
      val v = attributeUmarshaller.value(item, witness.value.name)
      val t = itemUnmarshallerT.value(item)
      Apply[Validated].map2(v, t) { field[K](_) :: _ }
    }

  implicit def caseClassUnmarshaller[A, R <: HList](implicit
                                                    gen: LabelledGeneric.Aux[A, R],
                                                    unmarshaller: Lazy[ItemUnmarshaller[R]]
                                                   ): ItemUnmarshaller[A] =
    (item: Item) => unmarshaller.value(item).map(gen.from)
}
