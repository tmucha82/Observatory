package observatory

import scala.math._


case class Location(lat: Double, lon: Double) {

  def earthDistance(other: Location): Double = Earth.R * greatCircleDistance(other)

  /**
    * Calculate distance between two location on sphere in km
    *
    * @param other location that we want to calculate distance to this location from this
    * @return distance between this and other locations
    * @see https://en.wikipedia.org/wiki/Great-circle_distance
    */
  private def greatCircleDistance(other: Location): Double = {
    val diffLatitudeRadius = abs(other.lat - this.lat).toRadians
    val diffLongitudeRadius = abs(other.lon - this.lon).toRadians

    val a = pow(sin(diffLatitudeRadius / 2), 2) + cos(this.lat.toRadians) * cos(other.lat.toRadians) * pow(sin(diffLongitudeRadius / 2), 2)
    2 * atan2(sqrt(a), sqrt(1 - a))
  }
}

case class Color(red: Int, green: Int, blue: Int)

case class Station(stn: Option[String], wban: Option[String], location: Option[Location])

case class TemperatureRecord(stn: Option[String], wban: Option[String], date: MeasureDate, temperature: Double)

case class MeasureDate(year: Int, month: Int, day: Int)

object Earth {
  val R = 6372.8 // [km]
}



