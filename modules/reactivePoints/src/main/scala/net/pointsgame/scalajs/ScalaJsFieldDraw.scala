// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.scalajs

import scala.scalajs.js.annotation.JSExport
import scalatags.SvgTags._
import scalatags._
import scalatags.all._

@JSExport
object ScalaJsFieldDraw {

	@JSExport
	val justAField = {
		val gridSquareSize = rx.Var(20)
		val offset = rx.Rx(gridSquareSize() / 2)

		val sizeX = rx.Var(11)
		val sizeY = rx.Var(9)

		val totalWidth = rx.Rx((
				gridSquareSize() * (sizeX() - 1) + offset() * 2
				).toString)
		val totalHeight = rx.Rx((
				gridSquareSize() * (sizeY() - 1) + offset() * 2
				).toString)

		def coord(c: Int) = (offset() + c * gridSquareSize()).toString
		val strokeStyle = (style := "stroke:rgb(99,99,99); stroke-width:1")

		val horizontalGridLines = rx.Rx(0 until sizeY() map { y =>
			line(
				"y1".attr := coord(y),
				"y2".attr := coord(y),
				"x1".attr := "0",
				"x2".attr := totalWidth(),
				strokeStyle
			)
		})
		val verticalGridLines = rx.Rx(0 until sizeX() map { x =>
			line(
				"x1".attr := coord(x),
				"x2".attr := coord(x),
				"y1".attr := "0",
				"y2".attr := totalHeight(),
				strokeStyle
			)
		})

		val mySvg = rx.Rx(
			svg("height".attr := totalHeight(), "width".attr := totalWidth(), position.absolute)(
				horizontalGridLines(),
				verticalGridLines()
			)
		)

		mySvg()
	}

}
