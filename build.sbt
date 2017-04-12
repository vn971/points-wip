
version in ThisBuild := "0.1-SNAPSHOT"
scalaVersion in ThisBuild := "2.11.8"
scalacOptions in ThisBuild ++= Seq("-unchecked", "-feature", "-deprecation", "-Xfuture", "-Xcheckinit")

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"

// uncomment if you don't want to use your internet connection for SNAPSHOT updates:
// offline:=true

lazy val h2database = "com.h2database" % "h2" % "1.4.178"
lazy val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"
lazy val akka = "com.typesafe.akka" %% "akka-actor" % "2.3.2"
//lazy val fobo = "net.liftmodules" %% "fobo_3.0" % "1.2"
//lazy val liftCommon = "net.liftweb" %% "lift-common" % "3.0-RC3"
lazy val liftWebkit = "net.liftweb" %% "lift-webkit" % "3.0-RC3"
lazy val jetty = "org.eclipse.jetty" % "jetty-webapp" % "9.1.3.v20140225"
lazy val squeryl = "org.squeryl" %% "squeryl" % "0.9.6-RC4"
lazy val scalatest = "org.scalatest" %% "scalatest" % "2.1.6" % Test
lazy val monixVersion = "1.1"
lazy val sjsDomLib = "0.8.2"


lazy val webappDirectorySetting =
	resourceGenerators in Compile += task {
		val webappBase = sourceDirectory.value / "main" / "webapp"
		val managedBase = resourceManaged.value
		for {
			(from, to) <- webappBase ** "*" pair rebase(webappBase, managedBase / "main" / "webapp")
		} yield {
			Sync.copy(from, to)
			to
		}
	}

lazy val pointsgame = project.in(file("."))
	.aggregate(liftServer, changesJS, changesJVM, scalajsModule, gameEngineExperiments, gameEngine)

lazy val changes = crossProject.in(file("./modules/changes"))
		// .jvmSettings(Revolver.enableDebugging(port = 5005, suspend = true))
		.jvmSettings(libraryDependencies += "org.monifu" %% "monifu" % monixVersion)
		.jvmSettings(libraryDependencies += "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided")
		.jvmSettings(libraryDependencies += "net.pointsgame" %% "scala-macro-log" % "0.9.4")
		.jvmSettings(fork := true)
		.jsSettings(libraryDependencies += "org.scala-js" %%% "scalajs-dom" % sjsDomLib)
		.jsSettings(libraryDependencies += "org.monifu" %%% "monifu" % monixVersion)
		.jsSettings(libraryDependencies += "net.pointsgame" %%% "scala-macro-log" % "0.9.4")
lazy val changesJVM: Project = changes.jvm
lazy val changesJS: Project = changes.js

lazy val scalajsModule = project.in(file("./modules/scalajs"))
		.enablePlugins(ScalaJSPlugin)
		.settings(libraryDependencies += "com.lihaoyi" %%% "scalarx" % "0.2.8")
		.settings(libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.5.3")
		.settings(libraryDependencies += "org.scala-js" %%% "scalajs-dom" % sjsDomLib)

lazy val gameEngine = project.in(file("./modules/game-engine"))
		.settings(libraryDependencies ++= Seq(scalatest))

lazy val gameEngineExperiments = project.in(file("./modules/game-engine-experiments"))
		.settings(libraryDependencies ++= Seq(scalatest))


lazy val liftServer = project.in(file("./modules/lift-server/"))
		.dependsOn(gameEngine)
		.settings(Revolver.settings)
		.settings(
			libraryDependencies ++= Seq(h2database, logback, akka, liftWebkit, jetty, squeryl, scalatest),
			assemblyJarName in assembly := "pointsgame.jar",
			webappDirectorySetting,
			fork in Test := true,
			reStart := (reStart dependsOn (fullOptJS in(scalajsModule, Compile, fullOptJS)).toTask).evaluated,
			// Revolver.enableDebugging(port = 5005, suspend = false),
			assembly := (assembly dependsOn (test in Test)).value
		)

(crossTarget in(scalajsModule, Compile, fullOptJS)) := (sourceDirectory in(liftServer, Compile)).value / "webapp" / "js"
