package net.pointsgame.lift.snippet

import net.liftweb.common.Loggable
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js._
import net.pointsgame.db.Message
import net.pointsgame.helpers.HtmlHelpers
import net.pointsgame.lift.liftvar.{UserSession, UserSessionVar}

class UsernameChanger extends Loggable {
	def render = HtmlHelpers.stringFunc { inputString =>
		val s = inputString.take(Message.CHARACTER_LIMIT)
		if (!s.matches("\\s*")) {
			logger.debug(s"logging in as: $s")
			UserSessionVar.set(Some(UserSession(-1L, s, s)))
		}
		SetValById("usernameChangerInput", "") & JsCmds.Noop
	}


}
