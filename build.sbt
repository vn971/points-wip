
version in ThisBuild := "0.1-SNAPSHOT"
scalaVersion in ThisBuild := "2.11.10"
scalacOptions in ThisBuild ++= Seq("-unchecked", "-feature", "-deprecation", "-Xfuture", "-Xcheckinit")

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"

// uncomment if you don't want to use your internet connection for SNAPSHOT updates:
// offline:=true

lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
lazy val h2database = "com.h2database" % "h2" % "1.4.194"
lazy val akka = "com.typesafe.akka" %% "akka-actor" % "2.4.17"
//lazy val fobo = "net.liftmodules" %% "fobo_3.0" % "1.2"
//lazy val liftCommon = "net.liftweb" %% "lift-common" % "3.0-RC3"
lazy val liftWebkit = "net.liftweb" %% "lift-webkit" % "3.0.1"
lazy val jetty = "org.eclipse.jetty" % "jetty-webapp" % "9.4.3.v20170317"
lazy val squeryl = "org.squeryl" %% "squeryl" % "0.9.8"
lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
lazy val monixVersion = "2.2.4"
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
		.jvmSettings(libraryDependencies += "io.monix" %% "monix" % monixVersion)
		.jvmSettings(libraryDependencies += "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided")
		.jvmSettings(fork := true)
		.jsSettings(libraryDependencies += "org.scala-js" %%% "scalajs-dom" % sjsDomLib)
		.jsSettings(libraryDependencies += "io.monix" %%% "monix" % monixVersion)
		.jsSettings(jsDependencies += RuntimeDOM)
lazy val changesJVM: Project = changes.jvm
lazy val changesJS: Project = changes.js

lazy val scalajsModule = project.in(file("./modules/scalajs"))
		.enablePlugins(ScalaJSPlugin)
		.settings(libraryDependencies += "com.lihaoyi" %%% "scalarx" % "0.2.9")
		.settings(libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.5.5")
		.settings(libraryDependencies += "org.scala-js" %%% "scalajs-dom" % sjsDomLib)
		.settings(jsDependencies += RuntimeDOM)

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
			reStart := (reStart dependsOn (fastOptJS in(scalajsModule, Compile, fastOptJS)).toTask).evaluated,
			// Revolver.enableDebugging(port = 5005, suspend = false),
			assembly := (assembly dependsOn (test in Test)).value
		)

(crossTarget in(scalajsModule, Compile, fullOptJS)) := (sourceDirectory in(liftServer, Compile)).value / "webapp" / "js"
(crossTarget in(scalajsModule, Compile, fastOptJS)) := (sourceDirectory in(liftServer, Compile)).value / "webapp" / "js"
