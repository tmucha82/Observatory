package observatory

import observatory.Visualization._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers with Observatory {


  trait TestSet {
    val averageTemperature = Seq(
      (Location(1.0, 1.0), 10.0),
      (Location(20.0, 20.0), 20.0)
    )
  }

  test("distance between two given locations") {
    assert(0.0 === Location(1.0, 1.0).earthDistance(Location(1.0, 1.0)))
    assert(0.0 === Location(22.0, 10.0).earthDistance(Location(22.0, 10.0)))

    assert(0.2424670477208617 == Location(10.0, 10.0).earthDistance(Location(20.0, 20.0)) / Earth.R)
    assert(0.2424670477208617 === Location(20.0, 20.0).earthDistance(Location(10.0, 10.0)) / Earth.R)
    assert(0.23530045911535313 === Location(20.0, 20.0).earthDistance(Location(30.0, 30.0)) / Earth.R)
    assert(0.23530045911535313 === Location(30.0, 30.0).earthDistance(Location(20.0, 20.0)) / Earth.R)
  }

  test("predictTemperature for location exact as one of location") {
    new TestSet {
      assert(10.0 === predictTemperature(averageTemperature, Location(1.0, 1.0)))
      assert(20.0 === predictTemperature(averageTemperature, Location(20.0, 20.0)))
    }
    ()
  }

  test("interpolateColor") {
    new TestSet {
      // todo
    }
    ()
  }

}
