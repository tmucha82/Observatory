package observatory

import com.sksamuel.scrimage.{RGBColor, Pixel, Image}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  private val p = 3
  private val distanceDelta = 1 //[km]

  val imageWidth = 360
  val imageHeight = 180

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location     Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    val distances = temperatures.map {
      case (iLocation, temperature) => (location.earthDistance(iLocation), temperature)
    }
    val (minDistance, closestTemperature) = distances.minBy { case (distance, temperature) => distance }
    if (minDistance <= distanceDelta) closestTemperature
    else {
      val pDistances = distances.map { case (distance, temperature) => (math.pow(distance, -p), temperature) }
      pDistances.map { case (pDistance, temperature) => pDistance * temperature }.sum / pDistances.map { case (pDistance, _) => pDistance }.sum
    }
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value  The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    /**
      * Function which convert temperature to value of
      * https://en.wikipedia.org/wiki/Linear_interpolation
      *
      * @param startPoint (x0, y0)
      * @param endPoint   (x1, y1)
      * @return
      */
    def interpolate(startPoint: (Double, Double), endPoint: (Double, Double)): Double => Int = {
      (value: Double) => {
        val factor = (endPoint._2 - startPoint._2) / (endPoint._1 - startPoint._1)
        val p = startPoint._2 + factor * (value - startPoint._1)
        math.round(p).toInt
      }
    }

    val sortedPoints = points.toList.sortBy { case (temperature, _) => temperature }
    sortedPoints.zip(sortedPoints.tail).find { case ((temperature1, _), (temperature2, _)) => value >= temperature1 && value < temperature2 } match {
      case Some(((temperature1, color1), (temperature2, color2))) =>
        def interpolateBaseColor(baseColor1: Int, baseColor2: Int) = interpolate((temperature1, baseColor1), (temperature2, baseColor2))(value)

        Color(
          interpolateBaseColor(color1.red, color2.red),
          interpolateBaseColor(color1.green, color2.green),
          interpolateBaseColor(color1.blue, color2.blue)
        )
      case None if value < sortedPoints.head._1 => sortedPoints.head._2
      case _ => sortedPoints.last._2
    }
  }

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param colors       Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    def location(x: Int, y: Int): Location = Location((imageHeight / 2) - y, x - (imageWidth / 2))
    val locations = for {
      y <- 0 until imageHeight
      x <- 0 until imageWidth
    } yield location(x, y)

    val pixels = locations.par.map {
      case location =>
        val color = interpolateColor(colors, predictTemperature(temperatures, location))
        Pixel(RGBColor(color.red, color.green, color.blue))
    }

    Image(imageWidth, imageHeight, pixels.toArray)
  }
}

