package net.pointsgame.ai

//import akka.actor._
//import java.io._
//import net.pointsgame.lift.model._
//import scala.util.Random
//
//class AiProcess(val commandLineToExecute: String) extends Actor with ActorLogging {
//
//	var process: java.lang.Process = null
//	var writer: BufferedWriter = null
//	var messageNumber = 0
//
//	def write(text: String) = {
//		messageNumber += 1
//		val msgToWrite = ""+messageNumber+" "+text+"\n"
//		log.info(msgToWrite)
//		writer.write(msgToWrite)
//		writer.flush()
//	}
//
//	override def receive() = {
//		case AiInitWithBoardSize(x, y) => write("init "+x+" "+y+" 0") // random seed = 0
//		case AiGenMove(isRed) => write("gen_move "+color(isRed))
//		case AiGenMoveWithTime(isRed, milliseconds) => write("gen_move_with_time "+color(isRed))
//		case DotC(x, y, isRed) => write("play "+x+" "+y+" "+color(isRed)) // color=1
//		case RedDot(x, y) => write("play "+x+" "+y+" 1") // color=1
//		case BlueDot(x, y) => write("play "+x+" "+y+" 0") // color=0
//		case d: DotFromAi => context.parent ! d
//	}
//
//	override def preStart() {
//		process = Runtime.getRuntime().exec(commandLineToExecute)
//		Runtime.getRuntime().addShutdownHook(new Thread {
//			override def run() {
//				try {
//					writer.write("-1\n")
//					writer.flush()
//				} catch {
//					case t: IOException =>
//						log.error(t.toString)
//				}
//				process.destroy();
//			}
//		});
//
//		val reader = new BufferedReader(new InputStreamReader(process.getInputStream))
//		writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream))
//		val readerActor = context.actorOf(akka.actor.Props(new AiProcessReader(reader)), name = "reader")
//	}
//	def color(isRed: Boolean) = if (isRed) "1" else "0"
//}
//
//private class AiProcessReader(reader: BufferedReader) extends Actor with ActorLogging {
//	override def preStart() {
//		while (true) {
//			var line = reader.readLine()
//			val splitted = line.split(' ')
//			val commandName = splitted(0)
//			log.info(line)
//			if (commandName equalsIgnoreCase "play") {
//				if (splitted(3) == "1")
//					context.parent ! DotFromAi(splitted(1).toInt, splitted(2).toInt, isRed = true)
//				else
//					context.parent ! DotFromAi(splitted(1).toInt, splitted(2).toInt, isRed = false)
//			} else if (commandName equalsIgnoreCase "genmove") {
//				if (splitted(3) == "1")
//					context.parent ! RedDot(splitted(1).toInt, splitted(2).toInt)
//				else
//					context.parent ! BlueDot(splitted(1).toInt, splitted(2).toInt)
//			}
//		}
//	}
//	override def receive = { case _ => Unit }
//}
