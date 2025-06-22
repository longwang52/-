package Second.data.hdfs2mysql

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 房屋特征分析表导入模块
 *
 * 功能：将HDFS上的房屋特征分析结果导入MySQL的feature_stats表
 *
 * 表特点：
 * 1. 按面积区间(area_range)分组统计二手房信息
 * 2. 包含房源数量、平均价格、每平米均价等指标
 * 3. 按面积区间升序排列，便于分析不同面积段的价格分布
 * 4. 建立了area_range的唯一索引，确保数据不重复
 */
object FeatureStatsHdfsToMysql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("FeatureStatsHdfsToMysql")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    try {
      // 1. 从HDFS读取ORC文件
      val orcFilePath = "hdfs://master:9000/output/house_analysis/feature_stats/"
      println(s"正在从HDFS读取房屋特征统计ORC文件: $orcFilePath")

      val featureDF = spark.read.orc(orcFilePath)

      println("房屋特征统计数据Schema:")
      featureDF.printSchema()
      println("房屋特征统计数据样例:")
      featureDF.show(5)

      // 2. 配置MySQL连接信息
      val jdbcUrl = "jdbc:mysql://192.168.226.128:3306/house_analysis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
      val tableName = "feature_stats"

      val properties = new java.util.Properties()
      properties.setProperty("user", "root")
      properties.setProperty("password", "123456")
      properties.setProperty("driver", "com.mysql.jdbc.Driver")

      // 3. 写入MySQL数据库
      println(s"正在写入MySQL表: $tableName")

      featureDF.write
        .mode(SaveMode.Overwrite)
        .jdbc(jdbcUrl, tableName, properties)

      println("房屋特征统计数据导入MySQL成功完成!")
    } catch {
      case e: Exception =>
        println(s"发生错误: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      spark.stop()
    }
  }
}