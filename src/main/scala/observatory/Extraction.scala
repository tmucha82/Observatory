package observatory

import java.time.LocalDate

import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Dataset}

/**
  * 1st milestone: data extraction
  */
object Extraction extends Observatory {

  import sparkSession.implicits._

  implicit val localDateEncoder = org.apache.spark.sql.Encoders.kryo[LocalDate]


  /**
    * This method should return the list of all the temperature records converted in degrees Celsius
    * along with their date and location (ignore data coming from stations that have no GPS coordinates).
    * You should not round the temperature values.
    * The file paths are resource paths, so they must be absolute locations in your classpath
    * (so that you can read them with getResourceAsStream).
    * For instance, the path for the resource file 1975.csv is "/1975.csv".
    *
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    ???
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    ???
  }

  /**
    * @param stationsFile file with all stations
    * @return dataset with all stations
    */
  def stations(stationsFile: String): Dataset[Station] = {
    createDataFrameFormCvs(stationsFile, createStationSchema).map {
      case row =>
        val location = (Option(row.getAs[Double]("latitude")), Option(row.getAs[Double]("longitude"))) match {
          case (Some(latitude), Some(longitude)) => Some(Location(latitude, longitude))
          case _ => None

        }
        Station(Option(row.getAs[String]("stn")), Option(row.getAs[String]("wban")), location)
    }
  }

  def temperatures(year: Int, temperaturesFile: String): Dataset[TemperatureRecord] = {
    createDataFrameFormCvs(temperaturesFile, createTemperatureSchema).map {
      case row =>
        TemperatureRecord(Option(row.getAs[String]("stn")), Option(row.getAs[String]("wban")),
          MeasureDate(year, row.getAs[Int]("month"), row.getAs[Int]("day")), row.getAs[Double]("temperature"))
    }
  }

  def createStationSchema: StructType = {
    StructType(List(
      StructField("stn", StringType, nullable = true),
      StructField("wban", StringType, nullable = true),
      StructField("latitude", DoubleType, nullable = true),
      StructField("longitude", DoubleType, nullable = true)
    ))
  }

  def createTemperatureSchema: StructType = {
    StructType(List(
      StructField("stn", StringType, nullable = true),
      StructField("wban", StringType, nullable = true),
      StructField("month", IntegerType, nullable = false),
      StructField("day", IntegerType, nullable = false),
      StructField("temperature", DoubleType, nullable = false)
    ))
  }

  def createDataFrameFormCvs(cvsFile: String, schema: StructType): DataFrame = {
    sparkSession.read
      .format("csv")
      .option("header", "false")
      .option("mode", "DROPMALFORMED")
      .schema(schema)
      .csv(getResourcePath(cvsFile))
  }

  def celsiusDegree(fahrenheitDegree: Double): Double = {
    (fahrenheitDegree - 32) / 1.8
  }

  def getResourcePath(filePath: String): String = {
    this.getClass.getResource(filePath).getPath
  }
}
