// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.actor

import net.pointsgame.lift.comet.ChatComet
import net.pointsgame.lift.liftvar.UserSession

case class AddChatSubscription(chatComet: ChatComet)
case class RemoveChatSubscription(chatComet: ChatComet)

case class UserJoined(u: UserSession)
case class UserLeft(u: UserSession)
