package Second.data.hdfs2mysql

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 价格趋势表导入模块
 *
 * 功能：将HDFS上的价格趋势分析结果导入MySQL的price_trend表
 *
 * 表特点：
 * 1. 按年份(year)和市区(district)分组统计价格趋势
 * 2. 包含房源数量、平均价格、每平米均价、价格增长率等指标
 * 3. 按年份和市区升序排列，便于分析时间序列变化
 * 4. 建立了year和district的联合索引，便于按年份或区域查询
 */
object PriceTrendHdfsToMysql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("PriceTrendHdfsToMysql")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    try {
      // 1. 从HDFS读取ORC文件
      val orcFilePath = "hdfs://master:9000/output/house_analysis/price_trend/"
      println(s"正在从HDFS读取价格趋势分析ORC文件: $orcFilePath")

      val priceTrendDF = spark.read.orc(orcFilePath)

      println("价格趋势分析数据Schema:")
      priceTrendDF.printSchema()
      println("价格趋势分析数据样例:")
      priceTrendDF.show(5)

      // 2. 配置MySQL连接信息
      val jdbcUrl = "jdbc:mysql://192.168.226.128:3306/house_analysis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
      val tableName = "price_trend"

      val properties = new java.util.Properties()
      properties.setProperty("user", "root")
      properties.setProperty("password", "123456")
      properties.setProperty("driver", "com.mysql.jdbc.Driver")

      // 3. 写入MySQL数据库
      println(s"正在写入MySQL表: $tableName")

      priceTrendDF.write
        .mode(SaveMode.Overwrite)
        .jdbc(jdbcUrl, tableName, properties)

      println("价格趋势分析数据导入MySQL成功完成!")
    } catch {
      case e: Exception =>
        println(s"发生错误: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      spark.stop()
    }
  }
}