package net.pointsgame.lift.snippet

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util.Helpers._
import net.pointsgame.lift.model.PaperDrawing._
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface
import scala.xml._


object GameDrawer extends Loggable {

	case class SgfGame(sizeX: Int, sizeY: Int, moves: Seq[(Boolean, Int, Int)])

	private def template: NodeSeq = Templates("_templates" :: "staticPaper" :: Nil).openOr(NodeSeq.Empty)

	def getSgf(xml: NodeSeq) = {
		try {
			val sizeX = xml.\("game").\("@size-x").text.toInt
			val sizeY = xml.\("game").\("@size-y").text.toInt

			val moves: Seq[(Boolean, Int, Int)] = xml(0).child.collect {
				case n if n.label == "red" =>
					(true, n.\("@x").text.toInt, n.\("@y").text.toInt)
				case n if n.label == "blue" =>
					(false, n.\("@x").text.toInt, n.\("@y").text.toInt)
			}

			val sgfGame = SgfGame(sizeX, sizeY, moves)
			Full(sgfGame)
		} catch {
			case t: Exception =>
				logger.debug(t)
				Empty
		}
	}

	def render(xml: NodeSeq): NodeSeq = {
		getSgf(xml).dmap(<strong>data parsing error</strong>: NodeSeq) {
			sgf =>
				val pixelSizeX = (sgf.sizeX - 1) * gridSquareWidth + offset * 2
				val pixelSizeY = (sgf.sizeY - 1) * gridSquareWidth + offset * 2
				val widthHeightStyle = s"width: ${pixelSizeX}px; height: ${pixelSizeY}px;"
				val gridImgSrc = s"/img/grid/${sgf.sizeX}-${sgf.sizeY}.png"

				val engine: SingleGameEngineInterface = new SingleGameEngine(sgf.sizeX, sgf.sizeY)
				sgf.moves.foreach(m => engine.makeMove(m._2, m._3, m._1))

				("img [style+]" #> widthHeightStyle &
						"div [style+]" #> widthHeightStyle &
						"img [src]" #> gridImgSrc
						).apply(template)

		}
	}
}
