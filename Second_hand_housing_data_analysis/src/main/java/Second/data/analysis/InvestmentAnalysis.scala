package Second.data.analysis

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.sql.functions.expr

/**
 * 投资潜力分析模块
 * 使用聚类算法识别高投资潜力的小区
 */
object InvestmentAnalysis {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("InvestmentAnalysis")
      .master("local[2]")
      .getOrCreate()

    import spark.implicits._

    // 读取清洗后的数据
    val cleanDataPath = "hdfs://master:9000/flume/events/Second_data_analysis/clean"
    val df = spark.read.format("orc").load(cleanDataPath)

    // 计算各小区的关键指标（保留原始数值列）
    val communityMetrics = df.groupBy("district", "community")
      .agg(
        count("*").alias("transaction_count"),
        avg(expr("price / area")).alias("avg_price_per_sqm_raw"),  // 原始数值列
        stddev(expr("price / area")).alias("price_volatility_raw"),  // 原始数值列
        avg("year").alias("avg_build_year_raw"),  // 原始数值列
        expr("count(case when elevator = '有' then 1 end) / count(*)").alias("elevator_ratio_raw")  // 原始数值列
      )
      .filter($"transaction_count" >= 5)

    // 创建格式化显示的列（不影响后续计算）
    val formattedMetrics = communityMetrics
      .withColumn("avg_price_per_sqm", format_number($"avg_price_per_sqm_raw", 2))
      .withColumn("price_volatility", format_number($"price_volatility_raw", 2))
      .withColumn("avg_build_year", format_number($"avg_build_year_raw", 0))
      .withColumn("elevator_ratio", format_number($"elevator_ratio_raw", 2))

    // 准备聚类特征（使用原始数值列）
    val assembler = new VectorAssembler()
      .setInputCols(Array(
        "transaction_count",
        "avg_price_per_sqm_raw",
        "price_volatility_raw",
        "avg_build_year_raw",
        "elevator_ratio_raw"
      ))
      .setOutputCol("features")

    val featureDF = assembler.transform(formattedMetrics)

    // 特征标准化
    val scaler = new StandardScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setWithStd(true)
      .setWithMean(true)

    val scalerModel = scaler.fit(featureDF)
    val scaledData = scalerModel.transform(featureDF)

    // 使用K-means聚类识别投资潜力等级
    val kmeans = new KMeans()
      .setK(3)
      .setFeaturesCol("scaledFeatures")
      .setSeed(42)

    val model = kmeans.fit(scaledData)
    val predictions = model.transform(scaledData)

    // 解释聚类结果
    println("聚类中心:")
    model.clusterCenters.foreach(println)

    // 添加有意义的标签（显示格式化后的列）
    val investmentPotential = predictions
      .withColumn("potential_level",
        when($"prediction" === 0, "高潜力")
          .when($"prediction" === 1, "中潜力")
          .otherwise("低潜力"))
      .select("district", "community", "potential_level",
        "transaction_count", "avg_price_per_sqm", "price_volatility")

    println("\n小区投资潜力分析结果:")
    investmentPotential.orderBy(desc("transaction_count")).show(50, truncate = false)

    // 保存结果到HDFS
    val outputPath = "hdfs://master:9000/output/house_analysis/investment_potential"
    investmentPotential.write
      .mode("overwrite")
      .format("orc")
      .save(outputPath)

    spark.stop()
  }
}