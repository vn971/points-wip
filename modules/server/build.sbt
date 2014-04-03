import sbtassembly.Plugin.AssemblyKeys._
import spray.revolver.RevolverPlugin.Revolver


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
