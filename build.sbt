name := "s3thorp"

version := "0.3.0"

scalaVersion := "2.12.8"

// command line arguments parser
libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2"

// i/o stream processor
libraryDependencies += "co.fs2" %% "fs2-core" % "1.0.4"
libraryDependencies += "co.fs2" %% "fs2-io" % "1.0.4"

// AWS SDK
libraryDependencies += "com.github.j5ik2o" %% "reactive-aws-s3-core" % "1.1.3"
libraryDependencies += "com.github.j5ik2o" %% "reactive-aws-s3-cats" % "1.1.3"

// Logging
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.26"

// testing
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.7" % "test"

// recommended for cats-effects
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds",
  "-Ypartial-unification")

