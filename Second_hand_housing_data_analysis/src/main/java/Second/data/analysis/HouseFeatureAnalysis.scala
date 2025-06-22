package Second.data.analysis

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.expr

/**
 * 房屋属性分析模块
 * 主要功能：
 * 1. 分析户型、朝向、楼层、装修、电梯等属性对价格的影响
 * 2. 计算不同面积区间的价格分布
 */
object HouseFeatureAnalysis {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("HouseFeatureAnalysis")
      .master("local[2]")
      .getOrCreate()

    import spark.implicits._

    // 读取清洗后的数据
    val cleanDataPath = "hdfs://master:9000/flume/events/Second_data_analysis/clean"
    val df = spark.read.format("orc").load(cleanDataPath)

    // 1. 户型分析
    println("不同户型的价格分析:")
    val layoutStats = df.groupBy("layout")
      .agg(
        count("*").alias("count"),
        format_number(avg("price"), 2).alias("avg_price"),
        format_number(avg(expr("price / area")), 2).alias("avg_price_per_sqm")
      )
      .orderBy(desc("avg_price"))
    layoutStats.show(truncate = false)

    // 2. 朝向分析
    println("\n不同朝向的价格分析:")
    val orientationStats = df.groupBy("orientation")
      .agg(
        count("*").alias("count"),
        format_number(avg("price"), 2).alias("avg_price"),
        format_number(avg(expr("price / area")), 2).alias("avg_price_per_sqm")
      )
      .orderBy(desc("avg_price"))
    orientationStats.show(truncate = false)

    // 3. 装修情况分析
    println("\n不同装修情况的价格分析:")
    val decorationStats = df.groupBy("decoration")
      .agg(
        count("*").alias("count"),
        format_number(avg("price"), 2).alias("avg_price"),
        format_number(avg(expr("price / area")), 2).alias("avg_price_per_sqm")
      )
      .orderBy(desc("avg_price"))
    decorationStats.show(truncate = false)

    // 4. 电梯分析
    println("\n是否有电梯的价格分析:")
    val elevatorStats = df.groupBy("elevator")
      .agg(
        count("*").alias("count"),
        format_number(avg("price"), 2).alias("avg_price"),
        format_number(avg(expr("price / area")), 2).alias("avg_price_per_sqm")
      )
      .orderBy(desc("avg_price"))
    elevatorStats.show(truncate = false)

    // 5. 面积区间分析
    println("\n不同面积区间的价格分析:")
    val areaStats = df.withColumn("area_range",
        when($"area" <= 50, "0-50")
          .when($"area" <= 70, "50-70")
          .when($"area" <= 90, "70-90")
          .when($"area" <= 110, "90-110")
          .when($"area" <= 130, "110-130")
          .when($"area" <= 150, "130-150")
          .otherwise("150+"))
      .groupBy("area_range")
      .agg(
        count("*").alias("count"),
        format_number(avg("price"), 2).alias("avg_price"),
        format_number(avg(expr("price / area")), 2).alias("avg_price_per_sqm")
      )
      .orderBy("area_range")
    areaStats.show(truncate = false)

    // 保存结果到HDFS
    val outputPath = "hdfs://master:9000/output/house_analysis/feature_stats"
    areaStats.write
      .mode("overwrite")
      .format("orc")
      .save(outputPath)

    spark.stop()
  }
}