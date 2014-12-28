package net.pointsgame.lift.model


sealed class PaperEvent
case class RightConnection(isRed: Boolean, x: Int, y: Int) extends PaperEvent
case class BottomConnection(isRed: Boolean, x: Int, y: Int) extends PaperEvent

case class DotColored(isRed: Boolean, x: Int, y: Int) extends PaperEvent
case class Dot(x: Int, y: Int)
case object NewGame
case class Surrounding(isRed: Boolean, perimeter: List[Dot]) extends PaperEvent


case class DotFromAi(x: Int, y: Int, isRed: Boolean)

object PaperDrawing {
	val gridSquareWidth = 20

	val offset = gridSquareWidth / 2

	val dotWidth = 14

	def dotPixel(i: Int) = offset + gridSquareWidth * i - dotWidth / 2
	def surrPixel(i: Int) = offset + gridSquareWidth * i
}
