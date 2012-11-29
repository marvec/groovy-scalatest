package org.jboss.qa.test

import org.scalatest._
import org.jboss.qa.SuperUtil

class SuperUtilSuite extends FunSuite {

  test("SuperUtil add should return addition of two numbers") {
    assert(SuperUtil.add(2, 3) == 5)
  }
}
