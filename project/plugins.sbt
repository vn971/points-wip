addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0") // sbt command: gen-idea

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0") // eclipse with-sources=true


addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2") // ~re-start

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2") // assembly


resolvers += Resolver.url("scalaJs-sbt-releases",
  url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(
    Resolver.ivyStylePatterns)

resolvers += Resolver.url("scalaJs-sbt-snapshots",
  url("http://repo.scala-js.org/repo/snapshots/"))(
    Resolver.ivyStylePatterns)

addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.5.0-M3")
