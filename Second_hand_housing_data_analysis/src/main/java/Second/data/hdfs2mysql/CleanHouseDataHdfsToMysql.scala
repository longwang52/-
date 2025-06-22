package Second.data.hdfs2mysql

import org.apache.spark.sql.{SaveMode, SparkSession}

object CleanHouseDataHdfsToMysql {
  def main(args: Array[String]): Unit = {
    // 创建SparkSession，设置为本地模式运行
    val spark = SparkSession.builder()
      .appName("CleanHouseDataHdfsToMysql")
      .master("local[*]") // 本地模式运行，使用所有可用核心
      .getOrCreate()

    // 设置日志级别为WARN，减少输出信息
    spark.sparkContext.setLogLevel("WARN")

    try {
      // 1. 从HDFS读取ORC文件
      val orcFilePath = "hdfs://master:9000/flume/events/Second_data_analysis/clean/"
      println(s"正在从HDFS读取ORC文件: $orcFilePath")

      val houseDF = spark.read.orc(orcFilePath)

      // 打印schema和数据样例，用于调试
      println("数据Schema:")
      houseDF.printSchema()
      println("数据样例:")
      houseDF.show(5)

      // 2. 配置MySQL连接信息
      val jdbcUrl = "jdbc:mysql://192.168.226.128:3306/house_analysis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
      val tableName = "clean_house_data"

      val properties = new java.util.Properties()
      properties.setProperty("user", "root") // 替换为实际用户名
      properties.setProperty("password", "123456") // 替换为实际密码
      properties.setProperty("driver", "com.mysql.jdbc.Driver")

      // 3. 写入MySQL数据库
      println(s"正在写入MySQL表: $tableName")

      houseDF.write
        .mode(SaveMode.Append) // 使用追加模式，避免覆盖已有数据
        .jdbc(jdbcUrl, tableName, properties)

      println("数据导入MySQL成功完成!")
    } catch {
      case e: Exception =>
        println(s"发生错误: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      // 关闭SparkSession
      spark.stop()
    }
  }
}