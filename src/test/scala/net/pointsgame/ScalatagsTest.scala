// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame

import org.scalatest.FunSuite
import scalatags.SvgTags._
import scalatags._
import scalatags.all._
import net.pointsgame.scalajs.ScalaJsFieldDraw

class ScalatagsTest extends FunSuite {

	test("fake test to produce svg.html") {
		val justAField = ScalaJsFieldDraw.justAField()
		val xmlToSave = xml.Unparsed(
			html(
				head(meta(content := "text/html; charset=UTF-8")),
				body(justAField)
			).toString())
		xml.XML.save("./target/svg.html", xmlToSave)
		// xml.XML.save("./src/main/webapp/svg.html", xmlToSave)
	}

}
