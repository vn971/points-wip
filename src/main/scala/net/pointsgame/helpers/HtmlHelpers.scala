package net.pointsgame.helpers

import net.liftweb.common.Loggable
import net.liftweb.http.S
import net.liftweb.util.BindHelpers._
import scala.xml.NodeSeq


object HtmlHelpers extends Loggable {

	def stringFunc(f: String => Any) = S.fmapFunc(f)("* [name]" #> _)

	def seqMemoize[T](f: T => NodeSeq => NodeSeq) = new SeqMemoize[T] {
		private var lastNodeSeq = NodeSeq.Empty

		def list(t: Seq[T]) = { ns =>
			lastNodeSeq = ns
			t.flatMap(f(_)(ns))
		}

		def apply(t: T) = f(t)(lastNodeSeq)
	}

}


trait SeqMemoize[T] extends (T => NodeSeq) {

	def list(seq: Seq[T]): NodeSeq => NodeSeq

	def apply(t: T): NodeSeq
}


@deprecated("use seqMemoize instead", "0.0")
trait Memoize[T] extends (T => (NodeSeq => NodeSeq)) {
	def apply(t: T): NodeSeq => NodeSeq

	def applyAgain(t: T): NodeSeq
}
