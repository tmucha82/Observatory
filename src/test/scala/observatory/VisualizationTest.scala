package observatory

import observatory.Visualization._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers with Observatory {


  trait TestSet {
    val averageTemperatureSet1 = Seq(
      (Location(10.0, 10.0), 10.0),
      (Location(30.0, 30.0), 20.0)
    )

    val averageTemperatureSet2 = Seq(
      (Location(10.0, 0.0), 10.0),
      (Location(-10.0, 0.0), 20.0)
    )

    val averageTemperatureSet3 = Seq(
      (Location(0.0, 30.0), 10.0),
      (Location(0.0, 30.0), 20.0)
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
      assert(10.0 === predictTemperature(averageTemperatureSet1, Location(10.0, 10.0)))
      assert(20.0 === predictTemperature(averageTemperatureSet1, Location(30.0, 30.0)))
    }
    ()
  }

  test("predictTemperature for location which is not location of any station") {
    new TestSet {
      assert(15 === predictTemperature(averageTemperatureSet1, Location(20.0, 20.0)).round)
      assert(15 === predictTemperature(averageTemperatureSet2, Location(0.0, 0.0)).round)
      assert(15 === predictTemperature(averageTemperatureSet3, Location(0.0, 0.0)).round)
    }
    ()
  }
}
