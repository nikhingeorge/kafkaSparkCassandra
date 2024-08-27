package pack

import org.apache.spark.sql.types._

object schemas {

  // Schema for User data
  val userSchema: StructType = new StructType()
    .add("results", ArrayType(new StructType()
      .add("user", new StructType()
        .add("gender", StringType, nullable = true)
        .add("name", new StructType()
          .add("title", StringType, nullable = true)
          .add("first", StringType, nullable = true)
          .add("last", StringType, nullable = true))
        .add("location", new StructType()
          .add("street", StringType, nullable = true)
          .add("city", StringType, nullable = true)
          .add("state", StringType, nullable = true)
          .add("zip", IntegerType, nullable = true))
        .add("email", StringType, nullable = true)
        .add("username", StringType, nullable = true)
        .add("password", StringType, nullable = true)
        .add("salt", StringType, nullable = true)
        .add("md5", StringType, nullable = true)
        .add("sha1", StringType, nullable = true)
        .add("sha256", StringType, nullable = true)
        .add("registered", LongType, nullable = true)
        .add("dob", LongType, nullable = true)
        .add("phone", StringType, nullable = true)
        .add("cell", StringType, nullable = true)
        .add("DNI", StringType, nullable = true)
        .add("picture", new StructType()
          .add("large", StringType, nullable = true)
          .add("medium", StringType, nullable = true)
          .add("thumbnail", StringType, nullable = true)))))

  // Schema for Location data (separate example)
  val locationSchema: StructType = new StructType()
    .add("street", StringType, nullable = true)
    .add("city", StringType, nullable = true)
    .add("state", StringType, nullable = true)
    .add("zip", IntegerType, nullable = true)

  // Schema for another type of data (e.g., Product data)
  val productSchema: StructType = new StructType()
    .add("productId", StringType, nullable = true)
    .add("productName", StringType, nullable = true)
    .add("price", DoubleType, nullable = true)
    .add("quantity", IntegerType, nullable = true)

}
