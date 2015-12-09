package net.pointsgame.scalajs

import net.pointsgame.scalajs.JSHelpers._
import net.pointsgame.scalajs.PlayerGlobalVariables._
import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js
import scala.scalajs.js.JSApp
import scalatags.generic.Bundle


object JSHelpers {
	val isIE = dom.window.navigator.appName.contains("Microsoft Internet Explorer")
}

object MyTestingEntryPoint extends JSApp {
	val field1 = new JSPaper(25, 20, "field1", scalatags.JsDom)
	val field2 = new JSPaper(15, 10, "field2", scalatags.JsDom)

	def main(): Unit = {
		dom.document.getElementById("increaseFieldSize").asInstanceOf[js.Dynamic].onmousedown = increaseFieldSize
		dom.document.getElementById("decreaseFieldSize").asInstanceOf[js.Dynamic].onmousedown = decreaseFieldSize
	}
}


class JSPaper[ScalatagsBuilder, ScalatagsOutput <: ScalatagsFragT, ScalatagsFragT]
(
	val sizeX: Int, val sizeY: Int,
	val domId: String,
	val scalatagsScope: Bundle[ScalatagsBuilder, ScalatagsOutput, ScalatagsFragT]
	) {

	import scalatagsScope.all._
	import scalatagsScope.svgTags.{line, svg}

	//	private var lastPoint: rx.Var[Option[(Int, Int)]] = rx.Var(None)

	val rootDomElement = dom.document.getElementById(domId)

	val totalWidth = rx.Rx((
		gridSquareSize() * (sizeX - 1) + offset() * 2
		).toString)
	val totalHeight = rx.Rx((
		gridSquareSize() * (sizeY - 1) + offset() * 2
		).toString)

	rx.Obs(gridSquareSize) {
		println("reacting on gridSquareSize change")
		rootDomElement.setAttribute(name = "height", value = totalHeight())
		rootDomElement.setAttribute(name = "width", value = totalWidth())

		val verticalGridLines = 0 until sizeX map { x =>
			line(
				"x1".attr := coord(x),
				"x2".attr := coord(x),
				"y1".attr := "0",
				"y2".attr := totalHeight()
			)
		}
		val horizontalGridLines = 0 until sizeY map { y =>
			line(
				"y1".attr := coord(y),
				"y2".attr := coord(y),
				"x1".attr := "0",
				"x2".attr := totalWidth()
			)
		}

		def ieViewbox = if (isIE) {
			Some("viewBox".attr := s"0 0.5 ${totalWidth()} ${totalHeight()}")
		} else {
			None
		}

		val gridSvgToInsert = svg(
			"height".attr := totalHeight(),
			"width".attr := totalWidth(),
			border := "1px solid #EEEEEE",
			ieViewbox,
			horizontalGridLines,
			verticalGridLines
		)

		document.querySelector(s"#$domId .gridLines").innerHTML = gridSvgToInsert.toString
	}

}
