package Second.data.analysis

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.expr

/**
 * 价格趋势分析模块
 * 分析二手房价格随时间的变化趋势
 */
object PriceTrendAnalysis {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("PriceTrendAnalysis")
      .master("local[2]")
      .getOrCreate()

    import spark.implicits._

    // 读取清洗后的数据
    val cleanDataPath = "hdfs://master:9000/flume/events/Second_data_analysis/clean"
    val df = spark.read.format("orc").load(cleanDataPath)

    // 按年份和市区分析价格趋势
    val priceTrendByYearDistrict = df.groupBy("year", "district")
      .agg(
        count("*").alias("house_count"),
        format_number(avg("price"), 2).alias("avg_price"),
        format_number(avg(expr("price / area")), 2).alias("avg_price_per_sqm")
      )
      .orderBy("year", "district")

    println("按年份和市区的价格趋势:")
    priceTrendByYearDistrict.show(100, truncate = false)

    // 计算价格增长率
    val windowSpec = Window.partitionBy("district").orderBy("year")
    val priceTrendWithGrowth = priceTrendByYearDistrict
      .withColumn("prev_avg_price", lag("avg_price", 1).over(windowSpec))
      .withColumn("price_growth_rate",
        when($"prev_avg_price".isNull, 0.0)
          .otherwise(format_number(($"avg_price" - $"prev_avg_price") / $"prev_avg_price" * 100, 2)))
      .orderBy("district", "year")

    println("\n价格增长率分析:")
    priceTrendWithGrowth.show(100, truncate = false)

    // 保存结果到HDFS
    val outputPath = "hdfs://master:9000/output/house_analysis/price_trend"
    priceTrendWithGrowth.write
      .mode("overwrite")
      .format("orc")
      .save(outputPath)

    spark.stop()
  }
}