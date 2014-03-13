
name := "PointsgameServer"

version := "1.0"

scalaVersion := "2.10.3"

organization := "net.pointsgame"

description := "Web server for the game Points"


scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")


seq(com.earldouglas.xsbtwebplugin.WebPlugin.webSettings :_*)

Keys.`package` <<= (Keys.`package` in Compile) dependsOn (test in Test)

scanDirectories in Compile := Nil // using 0.2.4+ of the sbt web plugin

port in container.Configuration := 4141


fork := true


resolvers ++= Seq(
	"Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
)

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
	"org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910" % "container,test",
	"org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
	"org.squeryl" %% "squeryl" % "0.9.5-6",
	"org.scalatest" %% "scalatest" % "1.9.1" % "test"
)
