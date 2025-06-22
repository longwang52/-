package Second.data.hdfs2mysql

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 小区统计表导入模块
 *
 * 功能：将HDFS上的小区统计结果导入MySQL的community_stats表
 *
 * 表特点：
 * 1. 按小区(community)分组统计二手房信息
 * 2. 包含房源数量、平均价格、每平米均价、平均建造年份等指标
 * 3. 按房源数量降序排列，便于快速查看热门小区
 * 4. 建立了district和community的联合索引，便于按区域查询小区
 */
object CommunityStatsHdfsToMysql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("CommunityStatsHdfsToMysql")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    try {
      // 1. 从HDFS读取ORC文件
      val orcFilePath = "hdfs://master:9000/output/house_analysis/community_stats/"
      println(s"正在从HDFS读取小区统计ORC文件: $orcFilePath")

      val communityDF = spark.read.orc(orcFilePath)

      println("小区统计数据Schema:")
      communityDF.printSchema()
      println("小区统计数据样例:")
      communityDF.show(5)

      // 2. 配置MySQL连接信息
      val jdbcUrl = "jdbc:mysql://192.168.226.128:3306/house_analysis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
      val tableName = "community_stats"

      val properties = new java.util.Properties()
      properties.setProperty("user", "root")
      properties.setProperty("password", "123456")
      properties.setProperty("driver", "com.mysql.jdbc.Driver")

      // 3. 写入MySQL数据库
      println(s"正在写入MySQL表: $tableName")

      communityDF.write
        .mode(SaveMode.Overwrite) // 使用覆盖模式
        .jdbc(jdbcUrl, tableName, properties)

      println("小区统计数据导入MySQL成功完成!")
    } catch {
      case e: Exception =>
        println(s"发生错误: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      spark.stop()
    }
  }
}