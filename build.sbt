name := "spark-csv-cassandra-import"

packageSummary in Linux := "Example CSV Import"

packageDescription := "Example application that illustrates how to use Spark+Spark Cassandra Connector to import a CSV file"

maintainer in Linux := "James Kavanagh <james.kavanagh@datastax.com>"

version := "0.1"

scalaVersion := "2.10.5"

enablePlugins(JavaAppPackaging)

val SparkVersion = "1.2.0"
val SparkCassandraVersion = "1.2.0-rc3"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

libraryDependencies ++= Seq(
  ("org.apache.spark" %%  "spark-core"  % SparkVersion % "provided").
    exclude("org.eclipse.jetty.orbit", "javax.transaction").
    exclude("org.eclipse.jetty.orbit", "javax.mail").
    exclude("org.eclipse.jetty.orbit", "javax.activation").
    exclude("commons-beanutils", "commons-beanutils-core").
    exclude("commons-collections", "commons-collections").
    exclude("commons-collections", "commons-collections").
    exclude("com.esotericsoftware.minlog", "minlog")
)

libraryDependencies ++= Seq(
  "com.datastax.spark"  %%  "spark-cassandra-connector" % SparkCassandraVersion,
  "org.apache.spark" %% "spark-streaming" % SparkVersion,
  "org.apache.spark" %% "spark-sql" % SparkVersion,
  "com.databricks" %% "spark-csv" % "0.1.1"
)

