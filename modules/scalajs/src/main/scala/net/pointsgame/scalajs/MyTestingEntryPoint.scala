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
//	val field1 = JsPaper.bindGrid(25, 20, "field1")
//	val field2 = JsPaper.bindGrid(15, 10, "field2")
	dom.document.getElementById("increaseFieldSize").asInstanceOf[js.Dynamic].onmousedown = increaseFieldSize
	dom.document.getElementById("decreaseFieldSize").asInstanceOf[js.Dynamic].onmousedown = decreaseFieldSize
}
