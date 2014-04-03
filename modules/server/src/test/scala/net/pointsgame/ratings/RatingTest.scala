package net.pointsgame.ratings

import net.liftweb.common.Loggable
import net.pointsgame.db.DBFunctions._
import net.pointsgame.db.DBLibrary._
import net.pointsgame.db.Pointsgame._
import net.pointsgame.db._
import net.pointsgame.ratings.RatingConstants._
import org.scalatest._

class RatingTest extends FunSuite with Loggable with BeforeAndAfterAll {

	test("probability simple check") {
		// 0.666
		assert(Ratings.probabilityToWin(1000L, 500L, 1.0) > 0.666)
		assert(Ratings.probabilityToWin(1000L, 500L, 1.0) < 0.7)

		// 0.333
		assert(Ratings.probabilityToLose(1000L, 500L, 1.0) > 0.333)
		assert(Ratings.probabilityToLose(1000L, 500L, 1.0) < 0.4)
	}

	override def beforeAll() {
		DBSetUp.setUp("jdbc:h2:mem:tests")
		DBSetUp.createDB()
	}

	override def afterAll() {
		DBSetUp.dropDB()
	}

	val user1 = DBUser(name = "1", id = 1)
	val user2 = DBUser(name = "2", id = 2)
	val user3 = DBUser(name = "3", id = 3)
	val user4 = DBUser(name = "4", id = 4, rating = humanToLong(3000))
	val user5 = DBUser(name = "5", id = 5)
	val user6 = DBUser(name = "6", id = 6, rating = humanToLong(3000))
	val nonExistentUserId = 999L // will never be inserted in DB

	test("insert users") {
		transaction {
			val inserted1 = users.insert(user1)
			assert(inserted1 == user1)
			assert(users.get(user1.id) == user1)
		}
		transaction {
			users.insert(user2)
		}
	}

	test("insert game") {
		transaction {
			val game = DBGame(firstId = user1.id, secondId = user2.id, wonFirst = Some(true))
			val insertedGame = games.insert(game)
			assert(game == insertedGame)
			assert(game.first.one == Some(user1))
			// assert(game.second.head == user2)
		}
	}
	test("insert game with non-existing first player") {
		intercept[RuntimeException] {
			transaction {
				games.insert(DBGame(firstId = user1.id, secondId = nonExistentUserId))
			}
		}
	}
	test("insert game with non-existing second player") {
		intercept[RuntimeException] {
			transaction {
				games.insert(DBGame(firstId = nonExistentUserId, secondId = user1.id))
			}
		}
	}

	test("find games") {
		assert(DBFunctions.findGames(user1.id).nonEmpty)
		assert(DBFunctions.findGames(user2.id).nonEmpty)
		assert(DBFunctions.findGames(nonExistentUserId).isEmpty)
	}

	test("every game increases the rating of the winner") {
		transaction {
			users.insert(user3)
			users.insert(user4)
		}

		var ratingBefore = transaction(users.get(user3.id).rating)

		for (i <- 1 to 3) {
			transaction {
				games.insert(DBGame(firstId = user3.id, secondId = user4.id, wonFirst = Some(true)))
			}
			Ratings.refineRating(userId = user3.id)

			val ratingNew = transaction(users.get(user3.id).rating)
			assert(ratingNew > ratingBefore)
			ratingBefore = ratingNew
		}
	}

	test("survives lots of games") {
		val gameList = (1 to 100).map(i => DBGame(firstId = user3.id, secondId = user4.id))
		transaction {
			games.insert(gameList)
		}
		Ratings.refineRating(userId = user3.id)
	}

	ignore("ratings don't grow too fast") {
		Ratings.refineRating(userId = user3.id)
		assert(getRating(userId = user3.id) > defaultRating * 3)
		assert(getRating(userId = user3.id) < defaultRating * 120)
	}

	test("ratings don't differ much after 1 game") {
		transaction {
			users.insert(user5)
			users.insert(user6)
		}
		transaction {
			games.insert(DBGame(firstId = user5.id, secondId = user6.id))
		}
		for (i <- 1 to 10) {
			Ratings.refineRating(userId = user5.id)
			Ratings.refineRating(userId = user6.id)
		}
		assert(getRating(user5.id) > defaultRating / 2)
		assert(getRating(user5.id) < defaultRating * 2)

		assert(getRating(user6.id) > defaultRating / 2)
		assert(getRating(user6.id) < defaultRating * 2)
	}

}
