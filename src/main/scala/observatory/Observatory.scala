package observatory

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession


trait Observatory {
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  @transient lazy val sparkSession = SparkSession.builder.master("local[8]").appName("Observatory").getOrCreate
}
