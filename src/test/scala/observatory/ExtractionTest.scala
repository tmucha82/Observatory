package observatory

import java.time.LocalDate

import observatory.Extraction._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Ignore, BeforeAndAfterAll, FunSuite}

@Ignore
@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    sparkSession.stop()
  }

  trait TestSet {
    val miniTestStationsPathFile = "/miniTestStations.csv"
    val miniTestTemperaturesOf2013PathFile = "/miniTestTemperatures.csv"

    val testYear = 2013
    val testStationsPathFile = "/testStations.csv"
    val testTemperaturesOf2013PathFile = s"/test$testYear.csv"

    val year = 2013
    val stationsPathFile = "/stations.csv"
    val temperaturesOf2013PathFile = s"/$year.csv"
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
    ()
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
    ()
  }

  test("temperatures for test file") {
    new TestSet {
      val temperaturesDataSet = temperatures(testYear, testTemperaturesOf2013PathFile)
      temperaturesDataSet.show(5)
      val firstTemperatures = temperaturesDataSet.take(5)
      assert(TemperatureRecord(Some("007018"), None, MeasureDate(2013, 7, 10), 84.2) === firstTemperatures(0))
      assert(TemperatureRecord(Some("007018"), None, MeasureDate(2013, 7, 11), 79.7) === firstTemperatures(1))
      assert(TemperatureRecord(Some("007018"), None, MeasureDate(2013, 7, 12), 74.4) === firstTemperatures(2))
      assert(TemperatureRecord(Some("722003"), Some("54930"), MeasureDate(2013, 1, 1), -4.0) === firstTemperatures(3))
      assert(TemperatureRecord(Some("722003"), Some("54930"), MeasureDate(2013, 1, 2), 15.2) === firstTemperatures(4))
    }
    ()
  }

  test("temperatures for big file") {
    new TestSet {
      val temperaturesDataSet = temperatures(year, temperaturesOf2013PathFile)
      temperaturesDataSet.show(5)
      val firstTemperatures = temperaturesDataSet.take(5)
      assert(TemperatureRecord(Some("007018"), None, MeasureDate(2013, 7, 10), 84.2) === firstTemperatures(0))
      assert(TemperatureRecord(Some("007018"), None, MeasureDate(2013, 7, 11), 79.7) === firstTemperatures(1))
      assert(TemperatureRecord(Some("007018"), None, MeasureDate(2013, 7, 12), 74.4) === firstTemperatures(2))
      assert(TemperatureRecord(Some("007018"), None, MeasureDate(2013, 7, 13), 76.9) === firstTemperatures(3))
      assert(TemperatureRecord(Some("007018"), None, MeasureDate(2013, 7, 14), 79.6) === firstTemperatures(4))
    }
    ()
  }

  test("stationTemperatures for test file") {
    new TestSet {
      val stationTemperaturesDataSet = stationTemperatures(stations(miniTestStationsPathFile).filter((station: Station) => station.location.isDefined), temperatures(testYear, miniTestTemperaturesOf2013PathFile))
      val orderedDataSet = stationTemperaturesDataSet.orderBy(stationTemperaturesDataSet("_1"))
      orderedDataSet.show()
      val result = orderedDataSet.collect()
      assert((MeasureDate(2013, 1, 29), Location(37.358, -78.438), 35.6) === result(0))
      assert((MeasureDate(2013, 8, 11), Location(37.35, -78.433), 81.14) === result(1))
      assert((MeasureDate(2013, 12, 6), Location(37.358, -78.438), 32.0) === result(2))
    }
    ()
  }

  test("locateTemperatures for test file") {
    new TestSet {
      val result = locateTemperatures(1975, miniTestStationsPathFile, miniTestTemperaturesOf2013PathFile).toList.sortWith((first, second) => first._1.isBefore(second._1))
      assert(Seq(
        (LocalDate.of(1975, 1, 29), Location(37.358, -78.438), 2.0),
        (LocalDate.of(1975, 8, 11), Location(37.35, -78.433), 27.3),
        (LocalDate.of(1975, 12, 6), Location(37.358, -78.438), 0.0)
      ) === result)
    }
    ()
  }

  test("locationYearlyAverageRecords for test file") {
    new TestSet {
      assert(Seq((Location(37.358, -78.438), 1.0), (Location(37.35, -78.433), 27.3)) === locationYearlyAverageRecords(Seq(
        (LocalDate.of(1975, 1, 29), Location(37.358, -78.438), 2.0),
        (LocalDate.of(1975, 8, 11), Location(37.35, -78.433), 27.3),
        (LocalDate.of(1975, 12, 6), Location(37.358, -78.438), 0.0)
      )))
    }
    ()
  }

}