package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers
import observatory.Interaction2._

@RunWith(classOf[JUnitRunner])
class Interaction2Test extends FunSuite with Checkers {

  test("availableLayers if return what it should") {
    assert(2 === availableLayers.size)
    assert(Seq(
      Layer(LayerName.Temperatures, temperatureColorPalette, 1975 to 1989),
      Layer(LayerName.Deviations, deviationColorPalette, 1990 to 2015)
    ) === availableLayers)
  }

  test("yearBounds if change Range according to layer") {
    val layerSignal = Var(availableLayers.head)
    val rangeSignal = yearBounds(layerSignal)
    assert(rangeSignal() === availableLayers.head.bounds)

    layerSignal() = availableLayers.tail.head
    assert(rangeSignal() === availableLayers.tail.head.bounds)
  }

  test("yearSelection for slider value in bounds") {
    val layerSignal = Var(availableLayers.head)
    val sliderValue = Var(Int.MinValue)
    val yearSignal = yearSelection(layerSignal, sliderValue)

    def checkYearInsideBound() = {
      layerSignal().bounds.foreach(year => {
        sliderValue() = year
        assert(year === yearSignal())
      })
    }

    checkYearInsideBound()
    layerSignal() = availableLayers.tail.head
    checkYearInsideBound()
  }

  test("yearSelection for slider value below bounds") {
    val layerSignal = Var(availableLayers.head)
    val sliderValue = Var(Int.MinValue)
    val yearSignal = yearSelection(layerSignal, sliderValue)
    val minYear = availableLayers.head.bounds.head

    assert(minYear === yearSignal())

    sliderValue() = minYear - 3
    assert(minYear === yearSignal())

    sliderValue() = minYear - 1
    assert(minYear === yearSignal())

    sliderValue() = minYear
    assert(minYear === yearSignal())
  }

  test("yearSelection for slider value above bounds") {
    val layerSignal = Var(availableLayers.head)
    val sliderValue = Var(Int.MaxValue)
    val yearSignal = yearSelection(layerSignal, sliderValue)
    val maxYear = availableLayers.head.bounds.last

    assert(maxYear === yearSignal())

    sliderValue() = maxYear + 3
    assert(maxYear === yearSignal())

    sliderValue() = maxYear + 1
    assert(maxYear === yearSignal())

    sliderValue() = maxYear
    assert(maxYear === yearSignal())
  }

}
