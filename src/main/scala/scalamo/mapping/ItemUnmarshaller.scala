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
  import cats.std.list._
  import shapeless._
  import shapeless.labelled.{FieldType, field}

  implicit val hnilItemUnmarshaller: ItemUnmarshaller[HNil] =
    new ItemUnmarshaller[HNil] {
      override def apply(item: Item): Validated[HNil] = HNil.valid
    }

  implicit def hconsItemUnmarshaller[K <: Symbol, V, T <: HList](implicit
                                                                 witness: Witness.Aux[K],
                                                                 attributeExtractor: Lazy[AttributeUnmarshaller[V]],
                                                                 itemUnmarshallerT: Lazy[ItemUnmarshaller[T]]
                                                                ): ItemUnmarshaller[FieldType[K, V] :: T] =
    new ItemUnmarshaller[FieldType[K, V] :: T] {
      override def apply(item: Item): Validated[FieldType[K, V] :: T] = {
        val v = attributeExtractor.value(item, witness.value.name)
        val t = itemUnmarshallerT.value(item)
        Apply[Validated].map2(v, t) { field[K](_) :: _ }
      }
    }

  implicit def caseClassUnmarshaller[A, R <: HList](implicit
                                                    gen: LabelledGeneric.Aux[A, R],
                                                    unmarshaller: Lazy[ItemUnmarshaller[R]]
                                                   ): ItemUnmarshaller[A] =
    new ItemUnmarshaller[A] {
      override def apply(item: Item): Validated[A] =
        unmarshaller.value(item).map(gen.from)
    }
}
