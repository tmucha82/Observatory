package observatory

import observatory.Extraction._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    sparkSession.stop()
  }

  trait TestSet {
    val testStationsPathFile = "/testStations.csv"
    val testTemperaturesOf2013PathFile = "/test2013.csv"

    val stationsPathFile = "/stations.csv"
    val temperaturesOf2013PathFile = "/2013.csv"
  }

  test("stations for test file") {
    new TestSet {
      val stationList = stations(testStationsPathFile)
      stationList.show(10)
    }
  }

  test("stations for big file") {
    new TestSet {
      stations(stationsPathFile)
    }
  }

  test("locateTemperatures for test file") {
    new TestSet {
      /**
        * Seq(
        * (LocalDate.of(2015, 8, 11), Location(37.35, -78.433), 27.3),
        * (LocalDate.of(2015, 12, 6), Location(37.358, -78.438), 0.0),
        * (LocalDate.of(2015, 1, 29), Location(37.358, -78.438), 2.0)
        * )
        */

      locateTemperatures(1975, testStationsPathFile, "")
    }
  }

  test("locateTemperatures for big file") {
    new TestSet {

      /**
        * Seq(
        * (Location(37.35, -78.433), 27.3),
        * (Location(37.358, -78.438), 1.0)
        * )
        */
      locateTemperatures(1975, "", "")
    }
  }

}