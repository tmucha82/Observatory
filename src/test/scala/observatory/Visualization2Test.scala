package observatory

import java.io.File

import observatory.Interaction._
import observatory.Visualization2._
import observatory.Visualization2.tileImageHeight
import observatory.Visualization2.tileImageWidth
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Visualization2Test extends FunSuite with Checkers {

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
    val file = new File(s"src/test/resources/${year}Tile2.png")
  }

  test("bilinearInterpolation for some test data") {
    assert(bilinearInterpolation(0.1, 0.5, 10, 20, 30, 40) === 17.0)
    assert(bilinearInterpolation(0.9, 0.1, 10, 20, 30, 40) === 29.0)
    assert(bilinearInterpolation(0.5, 0.5, 10, 20, 30, 40) === 25.0)
    assert(bilinearInterpolation(1.0, 0.0, 10, 20, 30, 40) === 30.0)
    assert(bilinearInterpolation(0.5, 0.1, 10, 20, 30, 40) === 21.0)
  }

  test("tile for test location") {
    new TestSet {
      val averageTemperatures = List((Location(0, 0), 32.0)).toIterable
      val grid = Manipulation.makeGrid(averageTemperatures)
      val image = visualizeGrid(grid, colorPalette, zoom, 0, 0)
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

  ignore("tile for parameters (zoom=2, x=1, y=1)") {
    new TestSet {
      lazy val temperatures = Extraction.locateTemperatures(year, stationsPath, temperaturePath)
      lazy val averageTemperatures = Extraction.locationYearlyAverageRecords(temperatures)
      val grid = Manipulation.makeGrid(averageTemperatures)

      val image = visualizeGrid(grid, colorPalette, zoom, 1, 1)
      image.output(file)
      assert(tileImageWidth === image.width)
      assert(tileImageHeight === image.height)

      image.output(file)
      assert(file.exists)
    }
    ()
  }

  ignore("generateTiles with data from 2015") {
    new TestSet {
      val temperaturesDirectory = new File("target/temperatures2")
      assert(if (temperaturesDirectory.exists()) temperaturesDirectory.exists() else temperaturesDirectory.mkdir())

      def saveImage(year: Int, zoom: Int, x: Int, y: Int, data: Iterable[(Location, Double)]) = {
        val zoomDirectory = new File(s"target/temperatures/$year/$zoom")
        assert(if (zoomDirectory.exists()) zoomDirectory.exists() else zoomDirectory.mkdirs())

        val target = new File(s"target/temperatures/$year/$zoom/$x-$y.png")
        println(target.getCanonicalPath)
        tile(data, colorPalette, zoom, x, y).output(target)
        ()
      }

      lazy val temperatures = Extraction.locateTemperatures(2015, stationsPath, temperaturePath)
      lazy val averageTemperatures = Extraction.locationYearlyAverageRecords(temperatures)
      val data = Set((2015, averageTemperatures))
      println("#1 generating tiles")
      generateTiles(data, saveImage)
    }
    ()
  }
}
