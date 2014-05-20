// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.lift.rest

import net.liftweb.common.Loggable
import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.util.Helpers._
import net.pointsgame.ai._
import net.pointsgame.lift.model.Dot

object MyRest extends RestHelper with Loggable {

	serve {
		case Get("api" :: "makemove" :: room :: AsInt(x) :: AsInt(y) :: Nil, req) =>
			logger.debug("making move: " + req.uri)
			AiSingleton.duel ! Dot(x, y)
			OkResponse()
	}
}


object RestPages extends RestHelper {
	serve {
		case Get("gameRoomAsRest" :: AsInt(roomId) :: Nil, req) =>
			for {
				session <- S.session
				template = Templates("gameRoom" :: Nil)
				response <- session.processTemplate(template, req, req.path, 200)
			} yield response
	}
}
