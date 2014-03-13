package net.pointsgame.ai

//import akka.actor._
//import ru.narod.vn91.pointsop.gameEngine._
//import net.pointsgame.lift.model._
//
//class Ai extends Actor {
//	val engine: SingleGameEngineInterface = new SingleGameEngine(30, 30);
//	val aiProcess: ActorRef = {
//		val commandLine = "./ai.linux"
//		context.actorOf(akka.actor.Props(new AiProcess(commandLine)), name = "process")
//	}
//
//	override def preStart() {
//		aiProcess ! AiInitWithBoardSize(30, 30)
//	}
//
//	def waitingAi: Receive = {
//		case DotFromAi(x, y, color) =>
//			engine.makeMove(x + 1, y + 1, color)
//			if (color)
//				liftGameData.red ! Dot(x, y)
//			else
//				liftGameData.blue ! Dot(x, y)
//			context.become(waitingUser, discardOld = true)
//	}
//
//	def waitingUser: Receive = {
//		case Dot(x, y) =>
//			if (engine.canMakeMove(x + 1, y + 1)) {
//				engine.makeMove(x + 1, y + 1, true)
//				context.become(waitingAi, discardOld = true)
//				aiProcess ! DotC(x, y, isRed = true)
//				aiProcess ! AiGenMoveWithTime(isRed = false, 3500L)
//			}
//		//		case NewGame =>
//		//			engine = new SingleGameEngine(30, 30);
//		//			DotsSingleton.red ! NewGame
//		//			DotsSingleton.blue ! NewGame
//	}
//
//	override def receive = waitingUser
//}
