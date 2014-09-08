// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package bootstrap.liftweb

import java.io.File
import net.liftweb.common.Loggable
import net.liftweb.util.Helpers.tryo
import net.liftweb.util.{LoggingAutoConfigurer, Props}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext


object Start extends App with Loggable {
	for {
		argument <- args
		if argument.startsWith("--run.mode=")
	} System.setProperty("run.mode", argument.split('=').last)

	LoggingAutoConfigurer().apply()

	startLift()

	/** Basic ways to start the jar are:
		* java -jar myjarname.jar
		* java -Drun.mode=production -jar myjarname.jar
		* java -Drun.mode=production -jar myjarname.jar 9980
		*/
	def startLift(): Unit = {
		logger.info("starting Lift server")

		val port: Int = {
			val argPort = tryo(args(0).toInt).filter(_ > 0).filter(_ < 65536)
			val propsPort = Props.getInt("liftweb.port")
			argPort.or(propsPort).openOr(4141)
		}

		val server = new Server(port)
		val context = new WebAppContext

		val webappDir = if (new File("src/main/webapp").exists()) {
			"src/main/webapp"
		} else {
			context.getClass.getClassLoader.getResource("webapp").toExternalForm
		}
		logger.info(s"using $webappDir as webappDir")

		context.setWar(webappDir)

		server.setHandler(context)
		server.start()
		logger.info(s"Lift server started, address is http://localhost:$port/")
	}

}
