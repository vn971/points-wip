package net.pointsgame.scalajs

import net.pointsgame.scalajs.PlayerGlobalVariables._
import net.pointsgame.scalajs.paper.JsPaper
import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js
import scala.scalajs.js.annotation._
import scalatags.generic.Bundle

@JSExportTopLevel("MyTestingEntryPoint")
object MyTestingEntryPoint {
	val increaseButton = dom.document.getElementById("increaseFieldSize")
	if (increaseButton != null) increaseButton.asInstanceOf[js.Dynamic].onmousedown = increaseFieldSize

	val decreaseButton = dom.document.getElementById("decreaseFieldSize")
	if (decreaseButton != null) decreaseButton.asInstanceOf[js.Dynamic].onmousedown = decreaseFieldSize
}
