package Second.data.hdfs2mysql

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
 * 投资潜力分析表导入模块
 *
 * 功能：将HDFS上的投资潜力分析结果导入MySQL的investment_potential表
 *
 * 表特点：
 * 1. 使用K-means聚类算法识别小区投资潜力等级(高/中/低)
 * 2. 包含小区交易数量、每平米均价、价格波动率等指标
 * 3. 按交易数量降序排列，便于优先查看交易活跃的小区
 * 4. 建立了district和community的联合索引，便于按区域查询
 */
object InvestmentPotentialHdfsToMysql {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("InvestmentPotentialHdfsToMysql")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    try {
      // 1. 从HDFS读取ORC文件
      val orcFilePath = "hdfs://master:9000/output/house_analysis/investment_potential/"
      println(s"正在从HDFS读取投资潜力分析ORC文件: $orcFilePath")

      val investmentDF = spark.read.orc(orcFilePath)

      println("投资潜力分析数据Schema:")
      investmentDF.printSchema()
      println("投资潜力分析数据样例:")
      investmentDF.show(5)

      // 2. 配置MySQL连接信息
      val jdbcUrl = "jdbc:mysql://192.168.226.128:3306/house_analysis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
      val tableName = "investment_potential"

      val properties = new java.util.Properties()
      properties.setProperty("user", "root")
      properties.setProperty("password", "123456")
      properties.setProperty("driver", "com.mysql.jdbc.Driver")

      // 3. 写入MySQL数据库
      println(s"正在写入MySQL表: $tableName")

      investmentDF.write
        .mode(SaveMode.Overwrite)
        .jdbc(jdbcUrl, tableName, properties)

      println("投资潜力分析数据导入MySQL成功完成!")
    } catch {
      case e: Exception =>
        println(s"发生错误: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      spark.stop()
    }
  }
}