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
}
