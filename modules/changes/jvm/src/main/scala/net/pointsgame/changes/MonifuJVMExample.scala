package net.pointsgame.changes

import java.util.concurrent.{Executors, ThreadFactory}
import monifu.concurrent.schedulers.AsyncScheduler
import monifu.concurrent.{Scheduler, UncaughtExceptionReporter}
import monifu.reactive._
import net.pointsgame.macros.SimpleLog._
import scala.concurrent.duration._
import scala.concurrent.forkjoin.ForkJoinPool
import scala.concurrent.{ExecutionContext, Future}
//import monix.execution.Scheduler.Implicits.global
//import monix.reactive._
//import monix.execution._

object MonifuJVMExample {
	import net.pointsgame.changes.Helper._

	def main(args: Array[String]): Unit = {
		log("starting JVM main class")

		// hack to create the non-daemon thread in monifu scheduler,
		// for the application not to exit early.
		implicitly[Scheduler].scheduleOnce(1.millis)(Unit)

		simpleSubscriptionTest()
		smartSubscriptionTest()
	}

	def simpleSubscriptionTest(): Unit = {
		EventGenerator.subscription.subscribe { userConnected ⇒
			log("simple subscription test", userConnected)
			Ack.Continue
		}
	}

	def smartSubscriptionTest(): Unit = {
		log(Thread.activeCount())
		val player: Observer[UserConnected] = new Observer[UserConnected] {
			override def onError(ex: Throwable): Unit = {
				ex.printStackTrace()
			}
			override def onComplete(): Unit = {
				log("subscription ended, yo-ho!")
				log(Thread.activeCount())

				// TODO: how to properly shut down the scheduler?
				scheduler.prepare()
				Helper.nonDaemonExecutor.shutdown()
			}
			override def onNext(uc: UserConnected): Future[Ack] = {
				log(Thread.activeCount())
				log("received event", uc)
				log(Thread.currentThread().isDaemon)
				Future.successful(Ack.Continue)
			}
		}
		EventGenerator.subscription.subscribe(player)
	}

}

object Helper {
	val ec = ExecutionContext.fromExecutor(new ForkJoinPool(1))

	val nonDaemonExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory {
		def newThread(r: Runnable): Thread = {
			val th = new Thread(r)
			th.setDaemon(false)
			th.setName("monifu-scheduler-nonDaemon")
			th
		}
	})

	implicit val scheduler: Scheduler = AsyncScheduler(
		nonDaemonExecutor,
		ec,
		UncaughtExceptionReporter.LogExceptionsToStandardErr
	)
}