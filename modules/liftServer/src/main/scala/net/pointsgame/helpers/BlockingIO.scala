package net.pointsgame.helpers

import scala.util.Try
import scala.io.Codec


object BlockingIO {

	def getFileAsLines(fileName: String) =
		Try {
			val source = io.Source.fromFile(fileName)(Codec.UTF8)
			val content = source.getLines().toVector
			source.close()
			content
		}

}
