package net.pointsgame.db

import net.liftweb.common.Loggable
import net.liftweb.http.LiftRules
import net.liftweb.util.Props
import net.pointsgame.db.Pointsgame._
import org.h2.jdbcx.JdbcConnectionPool
import org.squeryl.{ Session, SessionFactory }

object DBSetUp extends Loggable {

	def setUp(
			jdbcAddress: String = Props.get("jdbc.address").openOrThrowException("")
			): Unit = {
		logger.info(s"opening DB by address $jdbcAddress")
		val connPool = JdbcConnectionPool.create(jdbcAddress, "", "")

		LiftRules.unloadHooks.append { () =>
			logger.info("disposing connection pool")
			connPool.dispose()
		}

		SessionFactory.concreteFactory = Some(() =>
			Session.create(
				connPool.getConnection,
				new org.squeryl.adapters.H2Adapter))
	}

	def createDB(): Unit = {
		try {
			transaction {
				DBLibrary.create
			}
		} catch {
			case e: Exception =>
				logger.warn("DB population failed. (Tables were already defined?)")
		}
	}

	def dropDB(): Unit = {
		try {
			transaction {
				DBLibrary.drop
			}
		} catch {
			case e: Exception =>
				logger.warn("DB drop failed.")
		}
	}

}

