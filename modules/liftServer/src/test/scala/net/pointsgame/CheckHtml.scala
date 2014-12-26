package net.pointsgame

import org.scalatest.FunSuite

class CheckHtml extends FunSuite {

	/** Tests to make sure the project's XML files are well-formed.
		*
		* Finds every *.html and *.xml file in src/main/webapp (and its
		* subdirectories) and tests to make sure they are well-formed.
		*/
	test("html templates are correct") {
		def handled(fullName: String) = {
			val file = fullName.toLowerCase
			!file.endsWith("web.xml") && (
					file.endsWith(".html") || file.endsWith(".xml") ||
							file.endsWith(".htm") || file.endsWith(".xhtml"))
		}

		var broken: List[java.io.File] = Nil
		def collectBroken(file: java.io.File): Unit = {
			if (file.isDirectory) {
				for (f <- file.listFiles) collectBroken(f)
			}

			if (file.isFile && handled(file.getName)) {
				try {
					scala.xml.XML.loadFile(file)
				} catch {
					case e: org.xml.sax.SAXParseException => broken = file :: broken
				}
			}
		}

		collectBroken(new java.io.File("src/main/webapp"))

		if (broken.nonEmpty) {
			val msg = s"Malformed XML in ${broken.size} files: " + broken.mkString(", ")
			fail(msg)
		}
	}

}
