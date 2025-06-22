package Second.data.analysis

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler, StandardScaler}
import org.apache.spark.ml.regression.{RandomForestRegressor, RandomForestRegressionModel}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
 * 价格预测模型
 * 使用随机森林算法预测二手房价格
 */
object PricePredictionModel {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("PricePredictionModel")
      .master("local[2]")
      .getOrCreate()

    import spark.implicits._

    // 读取清洗后的数据
    val cleanDataPath = "hdfs://master:9000/flume/events/Second_data_analysis/clean"
    val df = spark.read.format("orc").load(cleanDataPath)

    // 数据类型转换：将floor和year列转换为数值型
    val dfWithCorrectTypes = df.withColumn("floor", col("floor").cast("double"))
      .withColumn("year", col("year").cast("double"))

    // 处理缺失值：填充缺失值
    val dfNoNulls = dfWithCorrectTypes.na.fill(0) // 可以根据需要选择不同的填充策略

    // 创建新特征：房屋年龄
    import org.apache.spark.sql.functions.current_date
    import org.apache.spark.sql.functions.year

    val currentYear = year(current_date())
    val dfWithAge = dfNoNulls.withColumn("age", currentYear - col("year"))

    // 数据预处理 - 将分类特征转换为数值索引
    val districtIndexer = new StringIndexer()
      .setInputCol("district")
      .setOutputCol("districtIndex")
      .setHandleInvalid("keep")

    val layoutIndexer = new StringIndexer()
      .setInputCol("layout")
      .setOutputCol("layoutIndex")
      .setHandleInvalid("keep")

    val orientationIndexer = new StringIndexer()
      .setInputCol("orientation")
      .setOutputCol("orientationIndex")
      .setHandleInvalid("keep")

    val decorationIndexer = new StringIndexer()
      .setInputCol("decoration")
      .setOutputCol("decorationIndex")
      .setHandleInvalid("keep")

    val elevatorIndexer = new StringIndexer()
      .setInputCol("elevator")
      .setOutputCol("elevatorIndex")
      .setHandleInvalid("keep")

    // 特征向量化
    val assembler = new VectorAssembler()
      .setInputCols(Array(
        "districtIndex", "layoutIndex",
        "orientationIndex", "decorationIndex",
        "elevatorIndex", "area", "age"
      ))
      .setOutputCol("assembledFeatures")

    // 标准化数值特征
    val scaler = new StandardScaler()
      .setInputCol("assembledFeatures")
      .setOutputCol("features")
      .setWithStd(true)
      .setWithMean(true)

    // 随机森林回归模型
    val rf = new RandomForestRegressor()
      .setLabelCol("price")
      .setFeaturesCol("features")
      .setNumTrees(50)  // 增加树的数量
      .setMaxDepth(15)  // 增加深度
      .setSeed(42)
      .setSubsamplingRate(0.8)  // 使用子采样
      .setFeatureSubsetStrategy("auto")  // 自动选择特征子集策略

    // 构建Pipeline
    val pipeline = new Pipeline()
      .setStages(Array(
        districtIndexer, layoutIndexer,
        orientationIndexer, decorationIndexer,
        elevatorIndexer, assembler, scaler, rf
      ))

    // 划分训练集和测试集
    val Array(trainingData, testData) = dfWithAge.randomSplit(Array(0.8, 0.2), seed = 42)

    // 训练模型
    val model = pipeline.fit(trainingData)

    // 在测试集上评估模型
    val predictions = model.transform(testData)
    predictions.select("price", "prediction").show(10)

    val evaluator = new RegressionEvaluator()
      .setLabelCol("price")
      .setPredictionCol("prediction")
      .setMetricName("rmse")

    val rmse = evaluator.evaluate(predictions)
    println(s"Root Mean Squared Error (RMSE) on test data = $rmse")

    // 获取特征重要性
    val rfModel = model.stages.last.asInstanceOf[RandomForestRegressionModel]
    println("Feature Importances:")
    rfModel.featureImportances.toArray.zip(Array(
      "district", "layout", "orientation",
      "decoration", "elevator", "area", "age"
    )).sortBy(-_._1).foreach { case (importance, feature) =>
      println(s"$feature: $importance")
    }

    // 保存模型
    val modelPath = "hdfs://master:9000/output/house_analysis/price_prediction_model"
    model.write.overwrite().save(modelPath)

    spark.stop()
  }
}