import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys
import sbtassembly.Plugin.AssemblyKeys._
import spray.revolver.RevolverPlugin.Revolver

lazy val commonSettings = Seq(
	version := "1.0",
	scalaVersion := "2.10.4",
	scalacOptions ++= Seq("-unchecked", "-feature", "-Xfuture", "-Xcheckinit"), // , "-Xlint"
	transitiveClassifiers in Global := Seq(Artifact.SourceClassifier), // don't download javadoc
	fork in Test := true
)

EclipseKeys.withSource := true // download sources for eclipse


sbtassembly.Plugin.assemblySettings

jarName in assembly := "pointsgame.jar"

assembly <<= assembly dependsOn (test in Test)

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


Revolver.settings.settings
//Revolver.enableDebugging(port = 5005, suspend = true)
//Revolver.enableDebugging(port = 5005, suspend = false)


resolvers ++= Seq(
	"Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
)

// uncomment if you don't want to use your internet connection for SNAPSHOT updates:
// offline:=true

lazy val scalatags = "com.scalatags" % "scalatags_2.10" % "0.2.4"
lazy val scalatagsJs = "com.scalatags" % "scalatags_2.10" % "0.2.4-JS"
lazy val scalarx = "com.scalarx" % "scalarx_2.10" % "0.2.3"
lazy val scalarxJs = "com.scalarx" % "scalarx_2.10" % "0.2.3-JS"
lazy val scalaJsDom = "org.scala-lang.modules.scalajs" %% "scalajs-dom" % "0.3"
lazy val h2database = "com.h2database" % "h2" % "1.3.175"
lazy val logback = "ch.qos.logback" % "logback-classic" % "1.0.13"
lazy val akka = "com.typesafe.akka" %% "akka-actor" % "2.3.0"
lazy val fobo = "net.liftmodules" %% "fobo_3.0" % "1.2"
lazy val liftWebkit = "net.liftweb" %% "lift-webkit" % "3.0-SNAPSHOT"
lazy val jetty = "org.eclipse.jetty" % "jetty-webapp" % "9.1.0.v20131115"
lazy val squeryl = "org.squeryl" %% "squeryl" % "0.9.6-RC2"
lazy val scalatest = "org.scalatest" %% "scalatest" % "2.1.0" % "test"
//lazy val gitDependency = uri("git://github.com/example/dependency.git#master")


lazy val reactivePoints = project.in(file("./modules/reactivePoints/")).
		settings(
			libraryDependencies ++= Seq(scalarxJs, scalatagsJs, scalaJsDom)
		).settings(
			commonSettings: _*
		)

lazy val server = project.in(file(".")).
		dependsOn(reactivePoints).
		settings(
			libraryDependencies ++= Seq(scalarxJs, scalatagsJs, scalaJsDom, h2database, logback, akka, fobo, liftWebkit, jetty, squeryl, scalatest)
		).settings(
			commonSettings: _*
		)
