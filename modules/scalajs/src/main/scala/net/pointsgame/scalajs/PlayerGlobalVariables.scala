package net.pointsgame.scalajs

import org.scalajs.dom.MouseEvent

object PlayerGlobalVariables {

	val gridSquareSize = rx.Var(20)

	val offset = rx.Rx(gridSquareSize() / 3)

	def coord(c: Int) = (offset() + c * gridSquareSize()).toString


	val increaseFieldSize = { (e: MouseEvent) =>
		println("increasing field size start")
		gridSquareSize() = gridSquareSize() + 1
		println("increasing field size end")
	}

	val decreaseFieldSize = { (e: MouseEvent) =>
		println("decreasing field size start")
		gridSquareSize() = gridSquareSize() - 1
		println("decreasing field size end")
	}

}
