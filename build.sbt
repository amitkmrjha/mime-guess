lazy val scala3Version = "3.1.1"
lazy val akkaHttpVersion = "10.2.7"
lazy val akkaVersion    = "2.6.18"

lazy val root = project
  .in(file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "mime-guess",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "net.java.dev.jna" % "jna" % "5.10.0",
      "net.java.dev.jna" % "jna-platform" % "5.10.0",
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.2.11"         % Test
    ),
  )