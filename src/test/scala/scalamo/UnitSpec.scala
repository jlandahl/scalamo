package scalamo

import org.scalatest._
import matchers.should._
import org.scalatest.{Inside, OptionValues}
import org.scalatest.flatspec.AnyFlatSpec

abstract class UnitSpec
  extends AnyFlatSpec
    with Matchers
    with OptionValues
    with Inside
