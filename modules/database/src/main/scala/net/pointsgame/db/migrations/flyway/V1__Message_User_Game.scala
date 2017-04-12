package net.pointsgame.db.migrations.flyway

import java.sql.Connection
import org.flywaydb.core.api.migration.jdbc.JdbcMigration

class V1__Message_User_Game extends JdbcMigration {

	val messageTable =
		"""
			|CREATE CACHED TABLE PUBLIC.MESSAGE(
			|    IP VARCHAR(128) NOT NULL,
			|    CONTENT VARCHAR(300) NOT NULL,
			|    TIME TIMESTAMP NOT NULL,
			|    ROOM VARCHAR(128) NOT NULL,
			|    USER VARCHAR(128) NOT NULL
			|)
		""".stripMargin

	val userTable =
		"""
			|
			|	CREATE CACHED TABLE PUBLIC.DBUSER(
			|    NAME VARCHAR(128) NOT NULL,
			|    LOWERCASE VARCHAR(128) NOT NULL,
			|    RATINGPRECISION DOUBLE NOT NULL,
			|    ID BIGINT DEFAULT NOT NULL,
			|    RATING BIGINT NOT NULL
			|)
		""".stripMargin

	val gameTable =
		"""
			|
			|CREATE CACHED TABLE PUBLIC.DBGAME(
			|    ID BIGINT DEFAULT NOT NULL,
			|    DATE TIMESTAMP NOT NULL,
			|    WONFIRST BOOLEAN,
			|    SECONDID BIGINT NOT NULL,
			|    FIRSTID BIGINT NOT NULL
			|)
		""".stripMargin

	override def migrate(connection: Connection): Unit = {
		Seq(messageTable, gameTable, userTable).foreach { sql ⇒
			val statement = connection.prepareStatement(sql)
			try {
				statement.execute()
			} finally {
				statement.close()
			}
		}
	}
}
