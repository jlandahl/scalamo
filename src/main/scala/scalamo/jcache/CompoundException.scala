package scalamo.jcache

import cats.data.NonEmptyList

case class CompoundException(causes: NonEmptyList[Throwable]) extends RuntimeException(causes.head) {
  override val getMessage: String =
    (causes.head +: causes.tail).mkString("; ")
}
