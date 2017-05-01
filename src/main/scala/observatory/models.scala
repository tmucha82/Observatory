package observatory

case class Location(lat: Double, lon: Double)

case class Color(red: Int, green: Int, blue: Int)

case class Station(stn: Option[String], wban: Option[String], location: Option[Location])

case class TemperatureRecord(stn: Option[String], wban: Option[String], date: MeasureDate, temperature: Double)

case class MeasureDate(year: Int, month: Int, day: Int)

