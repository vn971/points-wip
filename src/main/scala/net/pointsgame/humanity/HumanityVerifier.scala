// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.humanity

import net.liftweb.common.Loggable
import net.liftweb.http.SessionVar

/** "base" and "randomModule" have to be coprime
 *  this is true with a very high probability
 */
object Numbers extends Loggable {

	// the user must calculate such a "power" that
	// base^power (mod randomModule) < acceptableLimit
	val base = 717 // prime number
	// 36636
	def randomModule() = 59691 // 34000 + Random.nextInt(30000) // must be < 2^31
	val acceptableLimit = 111 // the smaller -- the harder

	/** one result with a harder limit would be OK in general,
	 *  but the time to solve would be less predictable.
	 */
	val numberOfResultsToConfirm = 10
}

object Helper {
	//	 calculates bas^pow (mod m).  This is quick even for a large "pow".
	def longPower(bas: Int, pow: Int, mod: Int): Int = {
		var result = 1
		var bitmask = 1 // bitmask for pow
		var basePowI = bas
		while (bitmask < mod) {
			if ((bitmask & pow) != 0) {
				result = (result * basePowI) % mod
			}
			bitmask = bitmask * 2
			basePowI = (basePowI * basePowI) % mod
		}
		result
	}
}

class MyHumanVerifier extends Loggable {

	import net.pointsgame.humanity.Helper._
	import net.pointsgame.humanity.Numbers._

	var mod = Numbers.randomModule()

	def isHuman: Boolean = (mod == 0)

	def check(power: String) {
		if (!isHuman && power.matches("[0-9]{1,11}")) check(power.toInt)
	}

	def check(power: Int) {
		if (!isHuman) {
			val exp = longPower(base, power, mod)
			if (exp <= acceptableLimit && power > 1) {
				mod = 0
				logger.debug("Confirmed that he is a human.")
			}
		}
	}

	def check(powerList: Set[Int]) {
		if (!isHuman &&
			powerList.size > numberOfResultsToConfirm &&
			powerList.forall { _ > 1 } &&
			powerList.forall { _ < randomModule } &&
			powerList.forall { longPower(base, _, mod) <= acceptableLimit }) {
			mod = 0
		}
	}
}

object HumanityVerifier extends SessionVar[MyHumanVerifier](new MyHumanVerifier)
