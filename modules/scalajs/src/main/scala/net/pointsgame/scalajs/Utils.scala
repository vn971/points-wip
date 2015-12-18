package net.pointsgame.scalajs

import org.scalajs.dom

object Utils {
	def isInternetExplorer = dom.window.navigator.appName.contains("Internet Explorer")
}
