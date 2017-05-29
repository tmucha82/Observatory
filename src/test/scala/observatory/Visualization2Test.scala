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
    val temperature2015Path = "/2015.csv"
    val file = new File(s"src/test/resources/${year}Tile2.png")
    val temperaturesPath = "target/temperatures2"
    val deviationsPath = "target/deviations"
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

  test("generateTiles with data from 2015") {
    new TestSet {
      val temperaturesDirectory = new File(temperaturesPath)
      assert(if (temperaturesDirectory.exists()) temperaturesDirectory.exists() else temperaturesDirectory.mkdir())

      def saveImage(year: Int, zoom: Int, x: Int, y: Int, data: (Int, Int) => Double) = {
        val zoomDirectory = new File(s"$temperaturesPath/$year/$zoom")
        assert(if (zoomDirectory.exists()) zoomDirectory.exists() else zoomDirectory.mkdirs())

        val target = new File(s"$temperaturesPath/$year/$zoom/$x-$y.png")
        println(target.getCanonicalPath)
        if (!target.exists()) {
          visualizeGrid(data, colorPalette, zoom, x, y).output(target)
        }
        ()
      }

      lazy val temperatures = Extraction.locateTemperatures(2015, stationsPath, temperature2015Path)
      lazy val averageTemperatures = Extraction.locationYearlyAverageRecords(temperatures)
      lazy val grid = Manipulation.makeGrid(averageTemperatures)
      val data = Set((2015, grid))
      println("#1 generating tiles")
      generateTiles(data, saveImage)
    }
    ()
  }

  /**
    * Once you have implemented the above methods, you are ready to generate the tiles showing the deviations
    * for all the years between 1990 and 2015, so that the final application (in the last milestone) will nicely display them:
    * - Compute normals from yearly temperatures between 1975 and 1989 ;
    * - Compute deviations for years between 1990 and 2015 ;
    * - Generate tiles for zoom levels going from 0 to 3, showing the deviations. Use the “output” method of “Image” to write the tiles on your file system, under a location named according to the following scheme: “target/deviations/<year>/<zoom>/<x>-<y>.png”.
    */
  ignore("generate the tiles showing the deviations") {
    new TestSet {
      val deviationsDirectory = new File(deviationsPath)
      assert(if (deviationsDirectory.exists()) deviationsDirectory.exists() else deviationsDirectory.mkdir())

      def saveImage(year: Int, zoom: Int, x: Int, y: Int, data: (Int, Int) => Double) = {
        val zoomDirectory = new File(s"$deviationsPath/$year/$zoom")
        assert(if (zoomDirectory.exists()) zoomDirectory.exists() else zoomDirectory.mkdirs())

        val target = new File(s"$deviationsPath/$year/$zoom/$x-$y.png")
        println(target.getCanonicalPath)
        if (!target.exists()) {
          visualizeGrid(data, colorPalette, zoom, x, y).output(target)
        }
        ()
      }

      println(s"Computing normals for 1975 - 1989")
      val normals = Manipulation.average((1975 to 1989).map {
        case yearToCalculate =>
          println(s"Calculating data for $yearToCalculate")
          lazy val temperatures = Extraction.locateTemperatures(yearToCalculate, stationsPath, s"/$yearToCalculate.csv")
          Extraction.locationYearlyAverageRecords(temperatures)
      })
      println(s"Computing deviations for year 2015")

      lazy val temperatures = Extraction.locateTemperatures(2015, stationsPath, temperature2015Path)
      lazy val averageTemperatures = Extraction.locationYearlyAverageRecords(temperatures)
      lazy val grid = Manipulation.deviation(averageTemperatures, normals)
      val data = Set((2015, grid))
      println("#1 generating tiles")
      generateTiles(data, saveImage)
    }
    ()
  }
}
