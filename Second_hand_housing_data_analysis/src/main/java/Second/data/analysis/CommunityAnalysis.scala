package Second.data.analysis

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.expr

/**
 * 小区分析模块
 * 主要功能：
 * 1. 按小区统计二手房信息
 * 2. 找出热门小区（房源数量多）
 */
object CommunityAnalysis {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("CommunityAnalysis")
      .master("local[2]")
      .getOrCreate()

    import spark.implicits._

    // 读取清洗后的数据
    val cleanDataPath = "hdfs://master:9000/flume/events/Second_data_analysis/clean"
    val df = spark.read.format("orc").load(cleanDataPath)

    // 按小区统计分析 - 使用expr计算每平米均价
    val communityStats = df.groupBy("district", "community")
      .agg(
        count("*").alias("house_count"),
        format_number(avg("price"), 2).alias("avg_price"),
        format_number(avg(expr("price / area")), 2).alias("avg_price_per_sqm"),
        format_number(avg("year"), 0).alias("avg_build_year")
      )
      .orderBy(desc("house_count"))

    println("各小区二手房统计信息(按房源数量降序):")
    communityStats.show(20, truncate = false)

    // 保存结果到HDFS
    val outputPath = "hdfs://master:9000/output/house_analysis/community_stats"
    communityStats.write
      .mode("overwrite")
      .format("orc")
      .save(outputPath)

    spark.stop()
  }
}