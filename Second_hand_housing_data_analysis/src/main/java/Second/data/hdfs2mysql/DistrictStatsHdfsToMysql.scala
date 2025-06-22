package Second.data.hdfs2mysql

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 区域统计表导入模块
 *
 * 功能：将HDFS上的区域统计结果导入MySQL的district_stats表
 *
 * 表特点：
 * 1. 按市区(district)分组统计二手房信息
 * 2. 包含房源数量、平均价格、最高价、最低价等关键指标
 * 3. 按房源数量降序排列，便于快速查看热门区域
 * 4. 建立了district的唯一索引，确保数据不重复
 */
object DistrictStatsHdfsToMysql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("DistrictStatsHdfsToMysql")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    try {
      // 1. 从HDFS读取ORC文件
      val orcFilePath = "hdfs://master:9000/output/house_analysis/district_stats/"
      println(s"正在从HDFS读取区域统计ORC文件: $orcFilePath")

      val districtDF = spark.read.orc(orcFilePath)

      println("区域统计数据Schema:")
      districtDF.printSchema()
      println("区域统计数据样例:")
      districtDF.show(5)

      // 2. 配置MySQL连接信息
      val jdbcUrl = "jdbc:mysql://192.168.226.128:3306/house_analysis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
      val tableName = "district_stats"

      val properties = new java.util.Properties()
      properties.setProperty("user", "root")
      properties.setProperty("password", "123456")
      properties.setProperty("driver", "com.mysql.jdbc.Driver")

      // 3. 写入MySQL数据库
      println(s"正在写入MySQL表: $tableName")

      districtDF.write
        .mode(SaveMode.Overwrite) // 使用覆盖模式，每次导入都是最新统计结果
        .jdbc(jdbcUrl, tableName, properties)

      println("区域统计数据导入MySQL成功完成!")
    } catch {
      case e: Exception =>
        println(s"发生错误: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      spark.stop()
    }
  }
}