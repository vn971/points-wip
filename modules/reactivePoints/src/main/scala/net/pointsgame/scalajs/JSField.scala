// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.scalajs

import net.pointsgame.scalajs.JSHelpers._
import net.pointsgame.scalajs.PlayerGlobalVariables._
import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scalatags.SvgTags._
import scalatags._

//object ServerSide {
//	val head_merge = "head_merge".tag
//
//	def renderHtml = {
//		head_merge()
//	}
//}


object PlayerGlobalVariables {
	val gridSquareSize = rx.Var(20)
	val offset = rx.Rx(PlayerGlobalVariables.gridSquareSize() / 3)

	def coord(c: Int) = (offset() + c * gridSquareSize()).toString
}


object JSHelpers {
	val isIE = dom.navigator.appName.contains("Microsoft Internet Explorer")
}

@JSExport
object MyTestingEntryPoint {
	val field1 = new JSField(25, 20, "field1")
	val field2 = new JSField(15, 10, "field2")

	@JSExport
	def main(): Unit = {
		dom.document.getElementById("increaseFieldSize").onmousedown = { (e: MouseEvent) =>
			println("+ start")
			gridSquareSize() = gridSquareSize() + 1
			println("+ end")
		}
		dom.document.getElementById("decreaseFieldSize").onmousedown = { (e: MouseEvent) =>
			println("- start")
			gridSquareSize() = math.max(0, gridSquareSize() - 1)
			println("- end")
		}
	}
}


class JSField(val sizeX: Int, val sizeY: Int, domId: String) {

	val rootDomElement = dom.document.getElementById(domId)

	private def selectorSetInnerHtml(selector: String, innerHtml: String): Unit = {
		dom.document.querySelector(selector).asInstanceOf[js.Dynamic].innerHTML = innerHtml
	}

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
			ieViewbox,
			horizontalGridLines,
			verticalGridLines
		).toString()

		//	jQuery.apply("#" + domId + " .gridLines").html(gridSvgToInsert)
		selectorSetInnerHtml("#" + domId + " .gridLines", gridSvgToInsert)
	}

}
