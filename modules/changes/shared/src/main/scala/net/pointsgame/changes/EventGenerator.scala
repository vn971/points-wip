package net.pointsgame.changes

import monifu.reactive._
import scala.concurrent.duration._

object EventGenerator {

	val subscription = Observable.intervalAtFixedRate(1.second).take(3).map {
		l ⇒ UserConnected(Channel(l), User(l))
	}

}
