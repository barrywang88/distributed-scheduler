name := "distributed-scheduler"

resolvers += Resolver.mavenLocal
resolvers ++= List("today nexus" at "http://nexus.today36524.td/repository/maven-public/")

lazy val commonSettings = Seq(
  organization := "com.barry",
  version := "1.0.5-SNAPSHOT",
  scalaVersion := "2.12.4"
)

javacOptions ++= Seq("-encoding", "UTF-8")

lazy val api = (project in file("distributed-scheduler-api"))
  .settings(
    commonSettings,
    name := "distributed-scheduler-api",
    libraryDependencies ++= Seq(
      "com.github.dapeng-soa" % "dapeng-client-netty" % "2.1.2-SNAPSHOT"
    )
  ).enablePlugins(ThriftGeneratorPlugin)


lazy val service = (project in file("distributed-scheduler-service"))
  .dependsOn( api )
  .settings(
    commonSettings,
    name := "distributed-scheduler_service",
    libraryDependencies ++= Seq(
      "com.github.dapeng-soa" % "dapeng-spring" % "2.1.2-SNAPSHOT",
      "com.github.wangzaixiang" %% "scala-sql" % "2.0.6",
      "org.slf4j" % "slf4j-api" % "1.7.13",
      "ch.qos.logback" % "logback-classic" % "1.1.3",
      "ch.qos.logback" % "logback-core" % "1.1.3",
      "org.codehaus.janino" % "janino" % "2.7.8", //logback (use if condition in logBack config file need this dependency)
      "mysql" % "mysql-connector-java" % "5.1.36",
      "com.alibaba" % "druid" % "1.0.17",
      "org.springframework" % "spring-context" % "4.3.5.RELEASE",
      "org.springframework" % "spring-tx" % "4.3.5.RELEASE",
      "org.springframework" % "spring-jdbc" % "4.3.5.RELEASE",
      "org.springframework" % "spring-test" % "4.3.5.RELEASE" % Test,
      "org.quartz-scheduler" % "quartz" % "2.2.1",
      "org.quartz-scheduler" % "quartz-jobs" % "2.2.1",
      "junit" % "junit" % "4.12" % Test,
      "com.today" %% "idgen-api" % "2.1.1" excludeAll("com.github.dapeng-soa","com.github.dapeng"),
      "javax.mail" % "mail" % "1.4",
      "org.reflections" % "reflections" % "0.9.11"
    )).enablePlugins(ImageGeneratorPlugin)
    .enablePlugins(DbGeneratePlugin)
  .enablePlugins(RunContainerPlugin)
