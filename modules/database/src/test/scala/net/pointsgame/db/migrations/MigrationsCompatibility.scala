package net.pointsgame.db.migrations

import net.pointsgame.db.{DBLibrary, PointsgameSqlTypes}
import org.scalatest._
import scalikejdbc._

class MigrationsCompatibility extends FunSuite {

	test("squeryl schema should equal to flyway-migrated DB") {
		val checkerDB = "jdbc:h2:mem:correctness_check;DB_CLOSE_DELAY=60"
		val squeryl = tablesListFromSqueryl
		val flyway = tablesListFromFlyway
		println(s"squeryl: \n$squeryl")
		println(s"flyway: \n$flyway")
		// sqlList.map(_.lines.mkString.toLowerCase.replaceAllLiterally(" ", ""))
		assert(cleanupNormalize(squeryl) == cleanupNormalize(flyway))
	}

	def tablesListFromSqueryl: List[String] = {
		val checkerDB = "jdbc:h2:mem:migration_compatibility_squeryl;DB_CLOSE_DELAY=60"
		new SquerylSchemaCreator(checkerDB).initialize()
		PointsgameSqlTypes.transaction {
			DBLibrary.create
		}
		getTablesSchema(checkerDB)
	}

	def tablesListFromFlyway: List[String] = {
		val checkerDB = "jdbc:h2:mem:migration_compatibility_flyway;DB_CLOSE_DELAY=60"
		new FlywayMigration().apply(checkerDB)
		getTablesSchema(checkerDB)
	}

	def cleanupNormalize(tables: List[String]): List[String] = {
		tables.filter(!_.contains("CREATE MEMORY TABLE PUBLIC.\"schema_version\"")).map {
			_.lines
				.map(_.replaceAll(
					"ID BIGINT DEFAULT \\(NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE.*",
					"ID BIGINT DEFAULT NULL,"
				))
				.mkString("\n")
		}
	}

	def getTablesSchema(dbAddress: String): List[String] = {
		Class.forName("org.h2.Driver")
		ConnectionPool.singleton(dbAddress, "", "")
		implicit val session = AutoSession
		val rawMap = sql"SELECT SQL FROM INFORMATION_SCHEMA.TABLES WHERE SQL IS NOT NULL".map(_.toMap()).list.apply()
		rawMap.flatMap(_.values).map(_.toString).sorted
	}

}
