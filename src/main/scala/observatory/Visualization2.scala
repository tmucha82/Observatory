package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Interaction.tileLocation
import observatory.Visualization.interpolateColor

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {

  val tileImageWidth = 256
  val tileImageHeight = 256
  val tileImageAlpha = 127

  /**
    * @param x   X coordinate between 0 and 1
    * @param y   Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(x: Double, y: Double, d00: Double, d01: Double, d10: Double, d11: Double): Double = {
    d00 * (1 - x) * (1 - y) + d10 * x * (1 - y) + d01 * (1 - x) * y + d11 * x * y
  }

  /**
    * @param grid   Grid to visualize
    * @param colors Color scale to use
    * @param zoom   Zoom level of the tile to visualize
    * @param x      X value of the tile to visualize
    * @param y      Y value of the tile to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
                     grid: (Int, Int) => Double,
                     colors: Iterable[(Double, Color)],
                     zoom: Int,
                     x: Int,
                     y: Int
                   ): Image = {
    def predictTemperature(location: Location) = {
      val latitude0 = location.lat.floor.toInt
      val latitude1 = location.lat.ceil.toInt
      val longitude0 = location.lon.floor.toInt
      val longitude1 = location.lon.ceil.toInt
      bilinearInterpolation(
        location.lat - latitude0, location.lon - longitude0,
        grid(latitude0, longitude0), grid(latitude0, longitude1), grid(latitude1, longitude0), grid(latitude1, longitude1)
      )
    }

    val pixels = {
      for {
        j <- 0 until tileImageHeight
        i <- 0 until tileImageWidth
      } yield interpolateColor(colors, predictTemperature(tileLocation(zoom + 8, tileImageWidth * x + i, tileImageHeight * y + j)))
    }.map(color => Pixel(color.red, color.green, color.blue, tileImageAlpha))

    Image(tileImageWidth, tileImageHeight, pixels.toArray)
  }
}
