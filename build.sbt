import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys
import sbtassembly.Plugin.AssemblyKeys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import scala.scalajs.sbtplugin.ScalaJSPlugin.ScalaJSKeys._
import spray.revolver.RevolverPlugin.Revolver
import spray.revolver.RevolverPlugin.Revolver._

lazy val commonSettings = Seq(
	version := "1.0",
	scalaVersion := "2.10.4",
	scalacOptions ++= Seq("-unchecked", "-feature", "-Xfuture", "-Xcheckinit"), // , "-Xlint"
	transitiveClassifiers in Global := Seq(Artifact.SourceClassifier), // don't download javadoc
	fork in Test := true,
	EclipseKeys.withSource := true // download sources for eclipse
)


resolvers ++= Seq(
	"Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
)

// uncomment if you don't want to use your internet connection for SNAPSHOT updates:
// offline:=true

lazy val scalatagsJs = "com.scalatags" % "scalatags_2.10" % "0.2.4-JS"
lazy val scalarxJs = "com.scalarx" % "scalarx_2.10" % "0.2.3-JS"
lazy val scalaJsDom = "org.scala-lang.modules.scalajs" %% "scalajs-dom" % "0.3"
lazy val h2database = "com.h2database" % "h2" % "[1.3.176,)"
lazy val logback = "ch.qos.logback" % "logback-classic" % "[1.1.1,)"
lazy val akka = "com.typesafe.akka" %% "akka-actor" % "2.3.0"
lazy val fobo = "net.liftmodules" %% "fobo_3.0" % "1.2"
lazy val liftWebkit = "net.liftweb" %% "lift-webkit" % "3.0-SNAPSHOT"
lazy val jetty = "org.eclipse.jetty" % "jetty-webapp" % "9.1.3.v20140225"
lazy val squeryl = "org.squeryl" %% "squeryl" % "0.9.6-RC2"
lazy val scalatest = "org.scalatest" %% "scalatest" % "2.1.0" % "test"
lazy val utest = "com.lihaoyi" % "utest_2.10" % "[0.1.2,)" % "test"
lazy val scalaJQuery = "org.scala-lang.modules.scalajs" %% "scalajs-jquery" % "0.3"


lazy val webappDirectorySetting =
	resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map { (managedBase, base) =>
		val webappBase = base / "src" / "main" / "webapp"
		for {
			(from, to) <- webappBase ** "*" pair rebase(webappBase, managedBase /
					"main" / "webapp")
		} yield {
			Sync.copy(from, to)
			to
		}
	}

lazy val reactivePoints = project.in(file("./modules/reactivePoints/")).
		settings(
			libraryDependencies ++= Seq(scalarxJs, scalatagsJs, scalaJsDom)
		).settings(
			commonSettings: _*
		).settings(
			scalaJSSettings: _*
		)

lazy val server = project.in(file("./modules/server/"))
		.dependsOn(reactivePoints)
		.settings(
			commonSettings: _*
		).settings(
			sbtassembly.Plugin.assemblySettings: _*
		).settings(
			Revolver.settings.settings: _*
		).settings(
			libraryDependencies ++= Seq(scalarxJs, scalatagsJs, scalaJsDom, h2database, logback, akka, liftWebkit, jetty, squeryl, scalatest),
			jarName in assembly := "pointsgame.jar",
			webappDirectorySetting,
			reStart <<= reStart dependsOn (optimizeJS in(reactivePoints, Compile, optimizeJS)),
			// Revolver.enableDebugging(port = 5005, suspend = false),
			assembly <<= assembly dependsOn (test in Test)
		)

(crossTarget in(reactivePoints, Compile, optimizeJS)) := (sourceDirectory in(server, Compile)).value / "webapp" / "js"
