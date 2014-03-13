package net.pointsgame.ai

import akka.actor._
import net.pointsgame.lift.comet._

object MyActorSys {
	//	val system = ActorSystem()
}

object liftGameData {
	private val system = ActorSystem()
	val ai = system.actorOf(akka.actor.Props[DuelWithEngine], name = "ai")

	//	val ai = system.actorOf(akka.actor.Props[Ai], name = "ai")
	//	val blue = new DotListSingleton()
	//	val red = new DotListSingleton()
}

//class GameBoardClass {
//	val ai = MyActorSys.system.actorOf(akka.actor.Props[DuelWithEngine])
//	val blue = new DotListSingleton()
//	val red = new DotListSingleton()
//	val rand = Random.nextInt()
//}

//object liftGameData extends SessionVar[GameBoardClass](new GameBoardClass())

//object vn971 extends SessionVar[String](""+Random.nextInt)
