package net.pointsgame.db.migrations

import org.flywaydb.core._

class FlywayMigration {

	def apply(url: String) = {
		val flyway = new Flyway()
		flyway.setLocations("classpath:net/pointsgame/db/migrations/flyway")
		flyway.setDataSource(url, null, null)
		flyway.migrate()
	}

}

object FlywayMigration extends App {
	new FlywayMigration().apply("jdbc:h2:file:./target/foobar;AUTO_SERVER=TRUE;")
}
