package observatory

import observatory.Interaction._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {

  trait TestSet {
    val zoom = 2
    val x = 0
    val y = 0

    val colorPalette = List(
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

  test("tileLocation for different coordinates and zooms") {
    assert(Location(85.05112877980659, -180.0) === tileLocation(0, 0, 0))

    assert(Location(85.05112877980659, -180.0) === tileLocation(1, 0, 0))
    assert(Location(0.0, -180.0) === tileLocation(1, 0, 1))
    assert(Location(85.05112877980659, 0.0) === tileLocation(1, 1, 0))
    assert(Location(0.0, 0.0) === tileLocation(1, 1, 1))

    assert(Location(51.512161249555156, 0.02197265625) === tileLocation(17, 65544, 43582))
    assert(Location(84.7383871209534, -176.484375) === tileLocation(10, 10, 10))
    assert(Location(-89.99999212633796, 945.0) === tileLocation(5, 100, 100))
  }

  test("tile for test location") {
    new TestSet {
      val averageTemperatures = List((Location(0, 0), 32.0)).toIterable
      val image = tile(averageTemperatures, colorPalette, zoom, x, y)
      assert(tileImageWidth === image.width)
      assert(tileImageHeight === image.height)
    }
    ()
  }
}
