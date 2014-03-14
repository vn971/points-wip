import sbtassembly.Plugin.AssemblyKeys._
import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys
import spray.revolver.RevolverPlugin.Revolver

name := "PointsgameServer"

version := "1.0"

scalaVersion := "2.10.3"

organization := "net.pointsgame"

description := "Web server for the game Points"


scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

transitiveClassifiers in Global := Seq(Artifact.SourceClassifier) // don't download javadoc

EclipseKeys.withSource := true // download sources for eclipse



sbtassembly.Plugin.assemblySettings

jarName in assembly := "pointsgame.jar"

assembly <<= assembly dependsOn (test in Test)

resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map
		{ (managedBase, base) =>
			val webappBase = base / "src" / "main" / "webapp"
			for {
				(from, to) <- webappBase ** "*" x rebase(webappBase, managedBase /
						"main" / "webapp")
			} yield {
				Sync.copy(from, to)
				to
			}
		}


Revolver.settings.settings

// Revolver.enableDebugging(port = 5005, suspend = false)


fork in Test := true


resolvers ++= Seq(
	"Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
)

// uncomment if you don't want to use your internet connection for SNAPSHOT updates:
// offline:=true

//val liftVersion = "2.5.1"
val liftVersion = "3.0-SNAPSHOT"

libraryDependencies ++= Seq(
	"ch.qos.logback" % "logback-classic" % "1.0.13",
	"com.h2database" % "h2" % "1.3.173",
	"com.typesafe.akka" % "akka-actor_2.10" % "2.3.0",
	"net.liftweb" %% "lift-common" % liftVersion,
	"net.liftweb" %% "lift-json" % liftVersion,
	"net.liftweb" %% "lift-util" % liftVersion,
	"net.liftweb" %% "lift-webkit" % liftVersion,
	"org.eclipse.jetty" % "jetty-webapp" % "9.1.0.v20131115",
//	"org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" artifacts Artifact("javax.servlet", "jar", "jar"),
	"org.squeryl" %% "squeryl" % "0.9.5-6",
	"org.scalatest" %% "scalatest" % "2.1.0" % "test"
)
