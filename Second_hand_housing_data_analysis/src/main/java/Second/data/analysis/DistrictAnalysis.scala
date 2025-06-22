package Second.data.analysis

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
 * 区域分析模块
 * 主要功能：
 * 1. 按市区统计二手房数量、平均价格、最高价、最低价
 * 2. 计算各市区二手房价格分布
 */
object DistrictAnalysis {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("DistrictAnalysis")
      .master("local[2]")
      .getOrCreate()

    import spark.implicits._

    // 读取清洗后的数据
    val cleanDataPath = "hdfs://master:9000/flume/events/Second_data_analysis/clean"
    val df = spark.read.format("orc").load(cleanDataPath)

    // 按市区统计分析
    val districtStats = df.groupBy("district")
      .agg(
        count("*").alias("house_count"),                      // 房源数量
        format_number(avg("price"), 2).alias("avg_price"),    // 平均价格
        format_number(max("price"), 2).alias("max_price"),    // 最高价格
        format_number(min("price"), 2).alias("min_price"),    // 最低价格
        format_number(avg("area"), 2).alias("avg_area")       // 平均面积
      )
      .orderBy(desc("house_count"))                           // 按房源数量降序排列

    println("各市区二手房统计信息:")
    districtStats.show(truncate = false)

    // 保存结果到HDFS
    val outputPath = "hdfs://master:9000/output/house_analysis/district_stats"
    districtStats.write
      .mode("overwrite")
      .format("orc")
      .save(outputPath)

    spark.stop()
  }
}