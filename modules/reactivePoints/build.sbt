import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._
import com.lihaoyi.workbench.Plugin._

scalaJSSettings

workbenchSettings

//ScalaJSKeys.optimizeJSPrettyPrint := true

//val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")

bootSnippet := "MyTestingEntryPoint().main();"

updateBrowsers <<= updateBrowsers.triggeredBy(ScalaJSKeys.packageJS in Compile)
