package net.pointsgame.lift.rest

import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.util.Helpers._
import net.pointsgame.ai._
import net.pointsgame.lift.model.Dot

object MyRest extends RestHelper {

	serve {
		case Get("api" :: "makemove" :: room :: AsInt(x) :: AsInt(y) :: Nil, req) =>
			liftGameData.ai ! Dot(x, y)
			OkResponse()
	}
}


object RestPages extends RestHelper {
	serve {
		case Get("gameRoomAsRest" :: AsInt(roomId) :: Nil, req) =>
			for {
				session <- S.session
				template = Templates("gameRoom" :: Nil)
				response <- session.processTemplate(template, req, req.path, 200 /* or a different HTTP response code here */)
			} yield response
	}
}
