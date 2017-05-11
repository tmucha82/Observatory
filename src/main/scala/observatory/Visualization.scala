package observatory

import com.sksamuel.scrimage.Image

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  private val p = 3
  private val distanceDelta = 1 //[km]

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
        p.round.toInt
      }

    }

    ???
  }

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    ???
  }
}

