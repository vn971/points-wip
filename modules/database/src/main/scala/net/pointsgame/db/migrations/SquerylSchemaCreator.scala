package net.pointsgame.db.migrations

import net.pointsgame.db.DBLibrary
import net.pointsgame.db.PointsgameSqlTypes._
import org.h2.jdbcx.JdbcConnectionPool
import org.slf4j.{Logger, LoggerFactory}
import org.squeryl._

class SquerylSchemaCreator(jdbcAddress: String) {

	val logger: Logger = LoggerFactory.getLogger(this.getClass)

	def initialize(): Unit = {
		logger.info(s"opening DB by address $jdbcAddress")
		val connPool = JdbcConnectionPool.create(jdbcAddress, "", "")

		sys.addShutdownHook {
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
			logger.warn("DB population failed. Were tables already defined?", e)
		}
	}

	def dropDB(): Unit = {
		try {
			transaction {
				DBLibrary.drop
			}
		} catch {
			case e: Exception =>
				logger.warn("DB drop failed.", e)
		}
	}

}
