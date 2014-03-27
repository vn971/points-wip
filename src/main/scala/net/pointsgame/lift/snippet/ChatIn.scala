// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.lift.snippet

import net.liftweb.common.Loggable
import net.liftweb.http._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js._
import net.pointsgame.actor.AkkaManager
import net.pointsgame.db.Message
import net.pointsgame.helpers.HtmlHelpers

class ChatIn extends Loggable {

	val room = S.uri.split('/').lastOption getOrElse ""
	val actor = AkkaManager.getRoom(room)

	def render = HtmlHelpers.stringFunc { inputString =>
		val s = inputString.take(Message.CHARACTER_LIMIT)
		if (!s.matches("\\s*")) {
			for (req <- S.request) {
				val msg = Message(room, "user", s, req.remoteAddr)
				logger.debug(s"sending chat: $msg")
				actor ! msg
			}
		}
		SetValById("chat_in", "") & JsCmds.Noop
	}

}

