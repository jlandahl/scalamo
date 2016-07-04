package scalamo

import org.scalatest.{FlatSpec, Inside, Matchers, OptionValues}

abstract class UnitSpec
  extends FlatSpec
    with Matchers
    with OptionValues
    with Inside
