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
      val stationDataSet = stations(testStationsPathFile)
      stationDataSet.show(5)
      val firstStations = stationDataSet.take(5)
      assert(Station(Some("010014"), None, Some(Location(59.792, 5.341))) === firstStations(0))
      assert(Station(Some("010015"), None, Some(Location(61.383, 5.867))) === firstStations(1))
      assert(Station(Some("010016"), None, Some(Location(64.850, 11.233))) === firstStations(2))
      assert(Station(Some("010017"), None, Some(Location(59.980, 2.250))) === firstStations(3))
      assert(Station(Some("010020"), None, Some(Location(80.050, 16.250))) === firstStations(4))
    }
  }

  test("stations for big file") {
    new TestSet {
      val stationDataSet = stations(stationsPathFile)
      stationDataSet.show(5)
      val firstStations = stationDataSet.take(5)
      assert(Station(Some("007005"), None, None) === firstStations(0))
      assert(Station(Some("007011"), None, None) === firstStations(1))
      assert(Station(Some("007018"), None, Some(Location(0.0d, 0.0d))) === firstStations(2))
      assert(Station(Some("007025"), None, None) === firstStations(3))
      assert(Station(Some("007026"), None, Some(Location(0.0d, 0.0d))) === firstStations(4))
    }
  }

  ignore("locateTemperatures for test file") {
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

  ignore("locateTemperatures for big file") {
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