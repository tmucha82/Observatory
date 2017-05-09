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
    val stationLocation = Location(1.0, 1.0)
  }

  test("distance between two given locations") {
    assert(0.0 === distance(Location(1.0, 1.0), Location(1.0, 1.0)))
    assert(0.0 === distance(Location(22.0, 10.0), Location(22.0, 10.0)))

    assert(1.1537002803174343 * Earth.R === distance(Location(10.0, 10.0), Location(20.0, 20.0)))
    assert(1.1537002803174343 * Earth.R === distance(Location(20.0, 20.0), Location(10.0, 10.0)))
    assert(1.0758009131045494 * Earth.R === distance(Location(20.0, 20.0), Location(30.0, 30.0)))
    assert(1.0758009131045494 * Earth.R === distance(Location(30.0, 30.0), Location(20.0, 20.0)))
  }

  ignore("predictTemperature for location exact as one of location") {
    new TestSet {
      assert(0.0 === predictTemperature(averageTemperature, stationLocation))
    }
    ()
  }

}
