package net.pointsgame.helpers

import net.liftweb.common.Loggable
import net.liftweb.http.S
import net.liftweb.util.Helpers._
import net.liftweb.util.CssSel
import scala.xml._


object HtmlHelpers extends Loggable {

	def stringFunc(f: String => Any): CssSel = S.fmapFunc(f)("* [name]" #> _)

	def seqMemoize[T](f: T => NodeSeq => NodeSeq) = new SeqMemoize[T] {
		private var lastNodeSeq = NodeSeq.Empty

		def list(t: Seq[T]) = { ns =>
			lastNodeSeq = ns
			t.flatMap(f(_)(ns))
		}

		def apply(t: T): NodeSeq = f(t)(lastNodeSeq)
	}

}


trait SeqMemoize[T] extends (T => NodeSeq) {
	def list(seq: Seq[T]): NodeSeq => NodeSeq
}
