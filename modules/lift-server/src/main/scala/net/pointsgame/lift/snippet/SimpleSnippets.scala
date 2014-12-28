package net.pointsgame.lift.snippet

import net.liftweb.common.Loggable
import net.liftweb.http._
import net.liftweb.util.CssSel
import net.liftweb.util.Helpers._

class SimpleSnippets extends Loggable {

	lazy val room = S.uri.split('/').lastOption getOrElse "TODO"

	def specifyRoom: CssSel = {
		if (room matches "[a-zA-Z0-9]{1,20}") {
			".specifyRoom [data-lift+]" #> s"&name=$room" &
					"* [class!]" #> "specifyRoom"
		} else {
			"*" #> "error constructing room"
		}
	}

	def lang = "* *" #> S.locale.toString

}
