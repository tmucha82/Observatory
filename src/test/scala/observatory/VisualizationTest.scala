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

    val colorPalette1 = List(
      (50.0, Color(0, 0, 0)),
      (100.0, Color(255, 255, 255)),
      (0.0, Color(255, 0, 127))
    )

    val colorPalette2 = List(
      (60.0, Color(255, 255, 255)),
      (32.0, Color(255, 0, 0)),
      (12.0, Color(255, 255, 0)),
      (0.0, Color(0, 255, 255)),
      (-15.0, Color(0, 0, 255)),
      (-27.0, Color(255, 0, 255)),
      (-50.0, Color(33, 0, 107)),
      (-60.0, Color(0, 0, 0))
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

  test("interpolateColor for every types of cases") {
    new TestSet {
      assert(Color(128, 0, 128) === interpolateColor(List((0.0, Color(255, 0, 0)), (1.0, Color(0, 0, 255))), 0.5))
      assert(Color(255, 0, 0) === interpolateColor(List((0.0, Color(255, 0, 0)), (1.0, Color(0, 0, 255))), 0.0))
      assert(Color(0, 0, 255) === interpolateColor(List((0.0, Color(255, 0, 0)), (1.0, Color(0, 0, 255))), 1.0))
      assert(Color(255, 0, 0) === interpolateColor(List((-4.197620741379907, Color(255, 0, 0)), (20.551397371699693, Color(0, 0, 255))), -14.197620741379907))
      assert(Color(0, 0, 255) === interpolateColor(List((-4.197620741379907, Color(255, 0, 0)), (20.551397371699693, Color(0, 0, 255))), 25))
      assert(Color(0, 0, 0) === interpolateColor(colorPalette1, 50.0))
      assert(Color(255, 0, 127) === interpolateColor(colorPalette1, 0.0))
      assert(Color(127, 127, 127) === interpolateColor(colorPalette1, 75.0))
      assert(Color(128, 0, 64) === interpolateColor(colorPalette1, 25.0))
      assert(Color(255, 0, 127) === interpolateColor(colorPalette1, -10.0))
      assert(Color(255, 255, 255) === interpolateColor(colorPalette1, 200.0))
    }
    ()
  }

  test("visualize for location and temperatures") {
    new TestSet {
      val temperatures: Iterable[(Location, Double)] = List()
      val image = visualize(temperatures, colorPalette2.toIterable)
      assert(360 === image.width)
      assert(180 === image.height)
    }
    ()
  }

}
