package net.pointsgame.changes

import monifu.concurrent.Implicits.globalScheduler
import monifu.reactive._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object MonifuJSExample extends JSApp {

	@JSExport
	def appendTextNode(): Unit = {
		import org.scalajs.dom.document
		val parNode = document.createElement("p")
		parNode.appendChild(document.createTextNode("hello from monifu @ pointsgame"))
		document.body.appendChild(parNode)
	}

	@JSExport
	def main(): Unit = {
		EventGenerator.subscription.subscribe { userConnected ⇒
			println("userConnected: " + userConnected)
			Ack.Continue
		}
	}

}
