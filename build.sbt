name := "Prototype for Jordan"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  Resolver.sonatypeRepo("releases")
)

val scalazV = "7.1.3"

val scalazStreamV = "0.8"

val logging = Seq (
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
)

val scalaz = Seq(
  "org.scalaz" %% "scalaz-core" % scalazV
)

val scalazStream = Seq(
  "org.scalaz.stream" %% "scalaz-stream" % scalazStreamV
)

libraryDependencies ++= scalaz ++ scalazStream
