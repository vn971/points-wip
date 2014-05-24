// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.lift.comet

import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.jquery.JqJsCmds.AppendHtml
import net.pointsgame.lift.model._
import net.pointsgame.lift.model.PaperDrawing._
import net.pointsgame.lift.comet.PaperSingleton.ListPaperEvent
import scala.xml.NodeSeq
import scala.xml.NodeSeq._
import net.liftweb.common.Loggable

class Paper extends CometActor with CometListener with Loggable {
	private var eventList: List[PaperEvent] = List()

	override protected def registerWith = PaperSingleton

	override def lowPriority = {
		case ListPaperEvent(newEventList) =>
			logger.trace(newEventList)
			val lastPrev = eventList.lastOption
			val update = newEventList.view.reverse.
					takeWhile(n => lastPrev.forall(_ != n)).
					map(eventToJs).
					fold(JsCmds.Noop)(_ & _)
			eventList = newEventList
			partialUpdate(update)
	}

	private def color(isRed: Boolean) = if (isRed) "red" else "blue"

	private def marginText(x: Int, y: Int) = "margin-left: " + dotPixel(x) + "px; margin-top: " + dotPixel(y) + "px;"

	private def eventToHtml(e: PaperEvent): (String, NodeSeq) = e match {
		case RightConnection(isRed, x, y) =>
			("connections", <img src={"/img/" + color(isRed) + "-right.png"} style={marginText(x, y)}/>: NodeSeq)
		case BottomConnection(isRed, x, y) =>
			("connections", <img src={"/img/" + color(isRed) + "-bottom.png"} style={marginText(x, y)}/>: NodeSeq)
		case DotColored(isRed, x, y) =>
			("dots", <img src={"/img/" + color(isRed) + ".png"} style={marginText(x, y)}/>: NodeSeq)
		case Surrounding(isRed, path) =>
			val stringPath = path.map(d => "" + surrPixel(d.x) + "," + surrPixel(d.y)).mkString(" ")
			val polygon = <svg xmlns="http://www.w3.org/2000/svg">
				<polygon points={stringPath} fill={color(isRed)}/>
			</svg>
			val polyline = <svg xmlns="http://www.w3.org/2000/svg">
				<polyline points={stringPath} stroke-color="black" stroke-width="3"/>
			</svg>
			("surroundings", polygon :: Nil)
		case _ => ("", <ok/>)
	}

	private def eventToJs(e: PaperEvent): JsCmd = {
		val (id, nodeSeq) = eventToHtml(e)
		AppendHtml(id, nodeSeq)
	}

	def render = {
		partialUpdate(eventList.map(eventToJs).fold(JsCmds.Noop)(_ & _))
		Empty
	}
}
