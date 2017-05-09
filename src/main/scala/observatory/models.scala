package observatory

import scala.math._


case class Location(lat: Double, lon: Double) {

  def earthDistance(other: Location): Double = point.earthDistance(other.point)

  lazy val point: Point = Point(toRadians(lat), toDegrees(lon))

}

case class Color(red: Int, green: Int, blue: Int)

case class Station(stn: Option[String], wban: Option[String], location: Option[Location])

case class TemperatureRecord(stn: Option[String], wban: Option[String], date: MeasureDate, temperature: Double)

case class MeasureDate(year: Int, month: Int, day: Int)

object Earth {
  val R = 6372.8 * 1000 // [m]
}

case class Point(latitudeRadius: Double, longitudeRadius: Double) {

  lazy val location: Location = Location(toDegrees(latitudeRadius), toDegrees(longitudeRadius))

  def earthDistance(other: Point): Double = {
    Earth.R * greatCircleDistance(other)
  }

  def greatCircleDistance(other: Point): Double = {
    val diffLatitudeRadius = abs(other.latitudeRadius - this.latitudeRadius)
    val diffLongitudeRadius = abs(other.longitudeRadius - this.longitudeRadius)

    val a = pow(sin(diffLatitudeRadius / 2), 2) + cos(this.latitudeRadius) * cos(other.latitudeRadius) * pow(sin(diffLongitudeRadius / 2), 2)
    2 * atan2(sqrt(a), sqrt(1 - a))
  }
}


