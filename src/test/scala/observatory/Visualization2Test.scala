package observatory

import observatory.Visualization2._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Visualization2Test extends FunSuite with Checkers {

  test("bilinearInterpolation for some test data") {
    assert(bilinearInterpolation(0.1, 0.5, 10, 20, 30, 40) === 17.0)
    assert(bilinearInterpolation(0.9, 0.1, 10, 20, 30, 40) === 29.0)
    assert(bilinearInterpolation(0.5, 0.5, 10, 20, 30, 40) === 25.0)
    assert(bilinearInterpolation(1.0, 0.0, 10, 20, 30, 40) === 30.0)
    assert(bilinearInterpolation(0.5, 0.1, 10, 20, 30, 40) === 21.0)
  }
}
