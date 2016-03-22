package net.pointsgame.humanity


/** "base" and "randomModule" have to be coprime
 *  this is true with a very high probability
 */
object Numbers {

	// the user must calculate such a "power" that
	// base^power (mod randomModule) < acceptableLimit
	val base = 719 // prime number
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
			bitmask = bitmask << 1
			basePowI = (basePowI * basePowI) % mod
		}
		result
	}
}

class MyHumanVerifier {

	import net.pointsgame.humanity.Helper._
	import net.pointsgame.humanity.Numbers._

	var mod = Numbers.randomModule()

	def isHuman: Boolean = { mod == 0 }

	def check(power: String): Unit = {
		if (!isHuman && power.matches("[0-9]{1,11}")) check(power.toInt)
	}

	def check(power: Int): Unit = {
		if (!isHuman) {
			val exp = longPower(base, power, mod)
			if (exp <= acceptableLimit && power > 1) {
				mod = 0
				println("Confirmed that he is a human.")
			}
		}
	}

	def check(powerList: Set[Int]): Unit = {
		if (!isHuman &&
			powerList.size > numberOfResultsToConfirm &&
			powerList.forall { _ > 1 } &&
			powerList.forall { _ < randomModule } &&
			powerList.forall { longPower(base, _, mod) <= acceptableLimit }) {
			mod = 0
		}
	}
}
