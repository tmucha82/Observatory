package observatory

import java.io.File

import observatory.Interaction._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {

  trait TestSet {
    val zoom = 2

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
    val year = 1975
    val stationsPath = "/stations.csv"
    val temperaturePath = s"/test$year.csv"
    val temperature2015Path = "/2015.csv"
    val file = new File(s"src/test/resources/${year}Tile1.png")
    val temperaturesPath = "target/temperatures1"
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
      val image = tile(averageTemperatures, colorPalette, zoom, 0, 0)
      assert(tileImageWidth === image.width)
      assert(tileImageHeight === image.height)
      image.pixels.foreach(pixel => {
        assert(255 === pixel.red)
        assert(0 === pixel.green)
        assert(0 === pixel.blue)
        assert(127 === pixel.alpha)
      })
    }
    ()
  }

  ignore("tile for tile (zoom=2, x=1, y=1)") {
    new TestSet {
      lazy val temperatures = Extraction.locateTemperatures(year, stationsPath, temperaturePath)
      lazy val averageTemperatures = Extraction.locationYearlyAverageRecords(temperatures)

      val image = tile(averageTemperatures, colorPalette, zoom, 1, 1)
      image.output(file)
      assert(tileImageWidth === image.width)
      assert(tileImageHeight === image.height)

      image.output(file)
      assert(file.exists)
    }
    ()
  }

  test("generateTiles with some test data") {
    new TestSet {
      def testData(year: Int, zoom: Int, x: Int, y: Int, data: (Int, Int, Int)) = println(s"$year, $zoom, $x, $y, $data")

      val data = Set((2001, (0, 0, 1)), (2002, (1, 1, 2)), (2003, (3, 2, 2)), (2004, (4, 2, 3)))
      generateTiles(data, testData)
    }
    ()
  }

  ignore("generateTiles with data from 2015") {
    new TestSet {
      val temperaturesDirectory = new File(temperaturesPath)
      assert(if (temperaturesDirectory.exists()) temperaturesDirectory.exists() else temperaturesDirectory.mkdir())

      def saveImage(year: Int, zoom: Int, x: Int, y: Int, data: Iterable[(Location, Double)]) = {
        val zoomDirectory = new File(s"$temperaturesPath/$year/$zoom")
        assert(if (zoomDirectory.exists()) zoomDirectory.exists() else zoomDirectory.mkdirs())

        val target = new File(s"$temperaturesPath/$year/$zoom/$x-$y.png")
        println(target.getCanonicalPath)
        tile(data, colorPalette, zoom, x, y).output(target)
        ()
      }

      lazy val temperatures = Extraction.locateTemperatures(2015, stationsPath, temperature2015Path)
      lazy val averageTemperatures = Extraction.locationYearlyAverageRecords(temperatures)
      val data = Set((2015, averageTemperatures))
      println("#1 generating tiles")
      generateTiles(data, saveImage)
    }
    ()
  }
}
