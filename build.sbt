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
	EclipseKeys.withSource := true // download sources for eclipse
)


resolvers ++= Seq(
	"Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
)

// uncomment if you don't want to use your internet connection for SNAPSHOT updates:
// offline:=true

lazy val h2database = "com.h2database" % "h2" % "1.4.178"
lazy val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"
lazy val akka = "com.typesafe.akka" %% "akka-actor" % "2.3.2"
lazy val fobo = "net.liftmodules" %% "fobo_3.0" % "1.2"
lazy val liftWebkit = "net.liftweb" %% "lift-webkit" % "3.0-M1"
lazy val jetty = "org.eclipse.jetty" % "jetty-webapp" % "9.1.3.v20140225"
lazy val squeryl = "org.squeryl" %% "squeryl" % "0.9.6-RC2"
lazy val scalatest = "org.scalatest" %% "scalatest" % "2.1.6" % Test

lazy val scalatagsJs = "com.scalatags" %%% "scalatags" % "0.2.5"
lazy val scalarxJs = "com.scalarx" %%% "scalarx" % "0.2.4"
lazy val scalaJsDom = "org.scala-lang.modules.scalajs" %% "scalajs-dom" % "0.4"
lazy val scalaJQuery = "org.scala-lang.modules.scalajs" %% "scalajs-jquery" % "0.3"
lazy val utest = "com.lihaoyi" %% "utest" % "0.2.3" % Test


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

lazy val reactivePoints = project.in(file("./modules/reactivePoints/"))
		.settings(scalaJSSettings: _*)
		.settings(commonSettings: _*)
		.settings(libraryDependencies ++= Seq(scalarxJs, scalatagsJs, scalaJsDom))

lazy val humanityVerifier = project.in(file("./modules/humanityVerifier"))
		.settings(commonSettings: _*)
		.settings(libraryDependencies ++= Seq(utest))
		.settings(testFrameworks += new TestFramework("utest.runner.JvmFramework"))

lazy val gameEngine = project.in(file("./modules/gameEngine"))
		.settings(commonSettings: _*)
		.settings(libraryDependencies ++= Seq(scalatest))

lazy val gameEngineExperiments = project.in(file("./modules/gameEngineExperiments"))
		.settings(commonSettings: _*)
		.settings(libraryDependencies ++= Seq(scalatest))


lazy val liftServer = project.in(file("./modules/liftServer/"))
		.dependsOn(gameEngine)
		.settings(commonSettings: _*)
		.settings(sbtassembly.Plugin.assemblySettings: _*)
		.settings(Revolver.settings.settings: _*)
		.settings(
			libraryDependencies ++= Seq(scalarxJs, scalatagsJs, scalaJsDom, h2database, logback, akka, liftWebkit, jetty, squeryl, scalatest),
			jarName in assembly := "pointsgame.jar",
			webappDirectorySetting,
			fork in Test := true,
			reStart <<= reStart dependsOn (fullOptJS in(reactivePoints, Compile, fullOptJS)),
			// Revolver.enableDebugging(port = 5005, suspend = false),
			assembly <<= assembly dependsOn (test in Test)
		)

(crossTarget in(reactivePoints, Compile, fullOptJS)) := (sourceDirectory in(liftServer, Compile)).value / "webapp" / "js"

