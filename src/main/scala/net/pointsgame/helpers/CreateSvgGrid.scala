package net.pointsgame.helpers

import net.pointsgame.lift.model.PaperDrawing._
import scala.xml._

object CreateSvgGrid extends App {

	create(5, 6)

	def create(sizeX: Int, sizeY: Int) {
		val xml = paperLinesSvg(sizeX, sizeY)
		XML.save(s"src/main/webapp/img/grid/howToCreate/$sizeX-$sizeY.svg", xml)
	}

	def paperLinesSvg(sizeX: Int, sizeY: Int) = {
		val pixelSizeX = (sizeX - 1) * gridSquareWidth + offset * 2
		val pixelSizeY = (sizeY - 1) * gridSquareWidth + offset * 2
		//		val widthHeightStyle = {
		//			"width: " + pixelSizeX + "px; height: " + pixelSizeY + "px;"
		//			""
		//		}
		def pixel(i: Int) = surrPixel(i).toString
		def horizontal(y: Int): Node =
				<line
				x1="0" y1={pixel(y)}
				x2={pixelSizeX.toString} y2={pixel(y)}
				style="stroke:rgb(128,128,128);stroke-width:1"/>
		def vertical(x: Int): Node =
				<line
				y1="0" x1={pixel(x)}
				y2={pixelSizeY.toString} x2={pixel(x)}
				style="stroke:rgb(128,128,128);stroke-width:1"/>

		val result = <svg xmlns="http://www.w3.org/2000/svg" style={"position: absolute; "}>
			{NodeSeq.fromSeq((0 until sizeY).map(horizontal))}{NodeSeq.fromSeq((0 until sizeX).map(vertical))}
		</svg>
		result: Node
	}

}
