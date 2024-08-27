package pack

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object structStreamingKafka {
  def main(args: Array[String]): Unit = {
    // Create SparkSession
    val spark = SparkSession.builder
      .appName("KafkaStructuredStreamingExample")
      .master("local[*]") // Use local[*] for local mode
      .getOrCreate()

    import spark.implicits._

    // Set log level to reduce verbosity
    spark.sparkContext.setLogLevel("ERROR")

    // Define Kafka parameters and read the stream
    val kafkaStream = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092") // Kafka broker
      .option("subscribe", "TutorialTopic") // Kafka topic
      .option("startingOffsets", "latest") // Start from earliest offset
      .load()

    // Convert the 'value' column to String (assuming the value contains JSON)
    val jsonStringDF = kafkaStream.selectExpr("CAST(value AS STRING) as jsonString")


    val jsonDF = jsonStringDF.select(from_json($"jsonString", schemas.userSchema).alias("data"))


    // Print the schema of the JSON data
    println("Inferred JSON Schema:")
    jsonDF.printSchema()

    // Optionally flatten the DataFrame for easier access to fields
    val flattenedDF = jsonDF.selectExpr("explode(data.results) as result")
      .select(
        $"result.user.gender",
        $"result.user.name.title",
        $"result.user.name.first",
        $"result.user.name.last",
        $"result.user.location.street",
        $"result.user.location.city",
        $"result.user.location.state",
        $"result.user.location.zip",
        $"result.user.email",
        $"result.user.username",
        $"result.user.phone",
        $"result.user.cell"
      )

    // Write the stream to the console (to see the JSON data)
    println("==================")

    val consoleQuery = flattenedDF.writeStream
      .outputMode("append")
      .format("console")
      .start()

    // Write the flattened DataFrame to Cassandra
    val cassandraQuery = flattenedDF.writeStream
      .outputMode("append")
      .foreachBatch { (batchDF: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row], batchId: Long) =>
        batchDF.write
          .format("org.apache.spark.sql.cassandra")
          .options(Map("table" -> "users_table", "keyspace" -> "your_keyspace"))
          .mode("append")
          .save()
      }
      .start()

    // Await termination
    consoleQuery.awaitTermination()
    cassandraQuery.awaitTermination()


  }
}
