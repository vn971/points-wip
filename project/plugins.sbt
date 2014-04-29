addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.2") // sbt command: gen-idea

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0") // eclipse with-sources=true


addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1") // ~re-start

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.1") // assembly



// scalajs-sbt-plugin
resolvers += Resolver.url("scala-js-releases",
  url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(
    Resolver.ivyStylePatterns)

// scalajs-sbt-plugin
resolvers += Resolver.url("scala-js-snapshots",
  url("http://repo.scala-js.org/repo/snapshots/"))(
    Resolver.ivyStylePatterns)

addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.4.2")


// workbench
resolvers += "spray repo" at "http://repo.spray.io"

addSbtPlugin("com.lihaoyi" % "workbench" % "0.1.2")
