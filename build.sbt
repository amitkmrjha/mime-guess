val scala3Version = "3.1.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "mime-guess",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
    libraryDependencies += "net.java.dev.jna" % "jna" % "5.10.0",
    libraryDependencies += "net.java.dev.jna" % "jna-platform" % "5.10.0"


  )