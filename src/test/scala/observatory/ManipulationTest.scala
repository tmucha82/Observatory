package observatory

import observatory.Manipulation._
import org.junit.runner.RunWith
import org.scalactic.TolerantNumerics
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class ManipulationTest extends FunSuite with Checkers {
  implicit val doubleEquality = TolerantNumerics.tolerantDoubleEquality(0.01)

  trait TestSet {
    val testTemperaturesList = Seq(
      Seq((Location(0, 10), 20.0), (Location(0, -10), 40.0)),
      Seq((Location(0, 10), 10.0), (Location(0, -10), 50.0)),
      Seq((Location(0, 10), 30.0), (Location(0, -10), 30.0))
    )
    val testTemperatures = testTemperaturesList.head
    val normalTemperatures = (latitude: Int, longitude: Int) => 10.0
  }

  test("makeGrid for test data") {
    new TestSet {
      def grid = makeGrid(testTemperatures)

      assert(30 === grid(0, 0))
      assert(30 === grid(10, 0))
      assert(30 === grid(20, 0))
      assert(30 === grid(-10, 0))
      assert(30 === grid(-20, 0))
      assert(20.0 === grid(0, 10))
      assert(21.66 === grid(10, 10))
      assert(25.34 === grid(20, 10))
      assert(40.0 === grid(0, -10))
      assert(38.33 === grid(10, -10))
      assert(34.65 === grid(20, -10))
    }
    ()
  }

  test("average for test data") {
    new TestSet {
      def temperatures = average(testTemperaturesList)

      assert(30 === temperatures(0, 0))
      assert(30 === temperatures(10, 0))
      assert(30 === temperatures(20, 0))
      assert(30 === temperatures(-10, 0))
      assert(30 === temperatures(-20, 0))
      assert(20.0 === temperatures(0, 10))
      assert(21.66 === temperatures(10, 10))
      assert(25.34 === temperatures(20, 10))
      assert(40.0 === temperatures(0, -10))
      assert(38.33 === temperatures(10, -10))
      assert(34.65 === temperatures(20, -10))
    }
    ()
  }

  test("deviation for test data") {
    new TestSet {
      def temperatures = deviation(testTemperatures, normalTemperatures)

      assert(20 === temperatures(0, 0))
      assert(20 === temperatures(10, 0))
      assert(20 === temperatures(20, 0))
      assert(20 === temperatures(-10, 0))
      assert(20 === temperatures(-20, 0))
      assert(10.0 === temperatures(0, 10))
      assert(11.66 === temperatures(10, 10))
      assert(15.34 === temperatures(20, 10))
      assert(30.0 === temperatures(0, -10))
      assert(28.33 === temperatures(10, -10))
      assert(24.65 === temperatures(20, -10))
    }
    ()
  }
}