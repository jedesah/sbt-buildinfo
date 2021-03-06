import scala.collection.immutable.::

lazy val check = taskKey[Unit]("checks this plugin")

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    name := "helloworld",
    version := "0.1",
    scalaVersion := "2.10.2",
    buildInfoKeys := Seq(
      name,
      version,
      scalaVersion
    ),
    buildInfoPackage := "hello",
    buildInfoOptions := Seq(BuildInfoOption.BuildTime),
    homepage := Some(url("http://example.com")),
    licenses := Seq("MIT License" -> url("https://github.com/sbt/sbt-buildinfo/blob/master/LICENSE")),
    check := {
      val f = (sourceManaged in Compile).value / "sbt-buildinfo" / ("%s.scala" format "BuildInfo")
      val lines = scala.io.Source.fromFile(f).getLines.toList
      lines match {
        case """package hello""" ::
          """""" ::
          """/** This object was generated by sbt-buildinfo. */""" ::
          """case object BuildInfo {""" ::
          """  /** The value is "helloworld". */"""::
          """  val name: String = "helloworld"""" ::
          """  /** The value is "0.1". */"""::
          """  val version: String = "0.1"""" ::
          """  /** The value is "2.10.2". */""" ::
          """  val scalaVersion: String = "2.10.2"""" ::
          builtAtStringComment ::
          builtAtString ::
          builtAtMillisComment ::
          builtAtMillis ::
          """  override val toString: String = {""" ::
          """    "name: %s, version: %s, scalaVersion: %s, builtAtString: %s, builtAtMillis: %s" format (""" ::
          """      name, version, scalaVersion, builtAtString, builtAtMillis""" ::
          """    )""" ::
          """  }""" ::
          """}""" :: Nil =>
        case _ => sys.error("unexpected output:\n" + lines.mkString("\n"))
      }
      ()
    }
  )
