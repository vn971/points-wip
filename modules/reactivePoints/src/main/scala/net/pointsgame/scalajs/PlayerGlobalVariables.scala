// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.scalajs

import scala.scalajs.js.annotation._
import org.scalajs.dom.MouseEvent

@JSExport
object PlayerGlobalVariables {

	val gridSquareSize = rx.Var(20)

	val offset = rx.Rx(gridSquareSize() / 3)

	def coord(c: Int) = (offset() + c * gridSquareSize()).toString


	@JSExport
	val increaseFieldSize = { (e: MouseEvent) =>
		println("increasing field size start")
		gridSquareSize() = gridSquareSize() + 1
		println("increasing field size end")
	}

	@JSExport
	val decreaseFieldSize = { (e: MouseEvent) =>
		println("decreasing field size start")
		gridSquareSize() = gridSquareSize() - 1
		println("decreasing field size end")
	}

}
