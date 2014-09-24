// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.lift.liftvar

import net.liftweb.http.SessionVar
import net.liftweb.common._

case class UserSession(dbId: Long, dbName: String, visualName: String)

object UserSessionVar extends SessionVar[Box[UserSession]](Empty)
