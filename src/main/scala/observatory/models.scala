package observatory

case class Location(lat: Double, lon: Double)

case class Color(red: Int, green: Int, blue: Int)

case class Station(stn: Option[String], wban: Option[String], location: Option[Location])

