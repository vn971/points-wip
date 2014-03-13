package net.pointsgame.humanity

import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import net.pointsgame.humanity.Helper.{longPower => power}
import net.liftweb.common.Loggable
import net.pointsgame.humanity.Numbers.base

class HumanityTest extends FunSuite with Loggable with ShouldMatchers with BeforeAndAfterAll {
	test("test") {
		val verifierClass = HumanityVerifier.is
		val timeStart = System.currentTimeMillis
		val mod = verifierClass.mod
		logger.info(s"mod=$mod")
		var power = 1
		var remainder = base
		while (remainder >= Numbers.acceptableLimit && !verifierClass.isHuman) {
			assert(power < 10000, "humanity takes too long to test, aborting")
			remainder = (remainder * base) % mod
			power += 1
			verifierClass.check(power)
		}
	}

	test("power tests") {
		assert(power(1, 1, 1000) == 1)
		assert(power(1, 2, 1000) == 1)
		assert(power(1, 10, 1000) == 1)

		assert(power(2, 1, 1000) == 2)
		assert(power(2, 2, 1000) == 4)
		assert(power(2, 10, 100000) == 1024)

		assert(power(2, 3, 8) == 0)

		assert(power(2, 2, 3) == 1)
		assert(power(2, 3, 3) == 2)
		assert(power(2, 4, 3) == 1)
		assert(power(2, 100, 3) == 1)
		assert(power(2, 101, 3) == 2)
		assert(power(2, 1000000000, 3) == 1)
	}

}
