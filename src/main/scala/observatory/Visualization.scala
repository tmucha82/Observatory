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

