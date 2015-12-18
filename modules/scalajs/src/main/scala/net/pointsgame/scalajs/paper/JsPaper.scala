package net.pointsgame.scalajs.paper

import net.pointsgame.scalajs.PlayerGlobalVariables._
import net.pointsgame.scalajs.Utils._
import org.scalajs.dom
import org.scalajs.dom._
import scala.scalajs.js.annotation.JSExport
import scalatags.Text.all._
import scalatags.Text.svgTags.{line, svg}

@JSExport
object JsPaper {

	@JSExport
	def bindGrid(sizeX: Int, sizeY: Int, domId: String) =
		rx.Obs(gridSquareSize) {
			val rootDomElement = dom.document.getElementById(domId)

			val totalWidth = rx.Rx(
				gridSquareSize() * (sizeX - 1) + offset() * 2
			)
			val totalHeight = rx.Rx(
				gridSquareSize() * (sizeY - 1) + offset() * 2
			)

			println("reacting on gridSquareSize change")
			// rootDomElement.setAttribute(name = "height", value = (totalHeight() - 9).toString)
			// rootDomElement.setAttribute(name = "width", value = (totalWidth()-9).toString)

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

			def ieViewbox = if (isInternetExplorer) {
				Some("viewBox".attr := s"0 0.5 ${totalWidth()} ${totalHeight()}")
			} else {
				None
			}

			val gridSvgToInsert = svg(
				"height".attr := totalHeight() - 1,
				"width".attr := totalWidth() - 1,
				border := "1px solid #EEEEEE",
				ieViewbox,
				horizontalGridLines,
				verticalGridLines
			)

			document.querySelector(s"#$domId .gridLines").innerHTML = gridSvgToInsert.toString()
		}

}
