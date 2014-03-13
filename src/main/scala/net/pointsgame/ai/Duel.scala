package net.pointsgame.ai

//import akka.actor._
//import net.pointsgame.lift.model._

//class Duel extends Actor {
//	var lastRed = true
//
//	override def receive = {
//		case Dot(x, y) =>
//			if (lastRed)
//				AiSingleton.blue ! Dot(x, y)
//			else
//				AiSingleton.red ! Dot(x, y)
//			lastRed = !lastRed
//		case BlueDot(x, y) =>
//			if (lastRed) {
//				AiSingleton.blue ! Dot(x, y)
//				lastRed = false
//			}
//		case RedDot(x, y) =>
//			if (!lastRed) {
//				AiSingleton.red ! Dot(x, y)
//				lastRed = true
//			}
//	}
//}
