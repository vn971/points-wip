package net.pointsgame.lift.liftvar

import net.liftweb.http.SessionVar
import net.liftweb.common._

case class UserSession(dbId: Long, dbName: String, visualName: String)

object UserSessionVar extends SessionVar[Box[UserSession]](Empty)
