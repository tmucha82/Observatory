package observatory

import java.time.LocalDate

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.types.{DoubleType, StringType, StructField, StructType}

/**
  * 1st milestone: data extraction
  */
object Extraction extends Observatory {

  import sparkSession.implicits._


  /**
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

    val stationSchema = StructType(List(
      StructField("STN", StringType, nullable = true),
      StructField("WBAN", StringType, nullable = true),
      StructField("Latitude", DoubleType, nullable = true),
      StructField("Longitude", DoubleType, nullable = true)
    ))

    val stationDataFrame = sparkSession.read
      .format("csv")
      .option("header", "false")
      .option("mode", "DROPMALFORMED")
      .schema(stationSchema)
      .csv(this.getClass.getResource(stationsFile).getPath)

    stationDataFrame.map(row =>
      Station(row.getAs[String]("STN"), row.getAs[String]("WBAN"), Location(row.getAs[Double]("Latitude"), row.getAs[Double]("Longitude"))))
  }
}
