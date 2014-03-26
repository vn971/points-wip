// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

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
