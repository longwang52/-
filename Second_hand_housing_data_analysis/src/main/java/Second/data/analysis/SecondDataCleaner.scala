package Second.data.analysis

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

/**
 * 二手房数据清洗模块
 * 主要功能：
 * 1. 读取原始二手房数据
 * 2. 进行数据清洗（处理脏数据）
 * 3. 保存清洗后的数据到HDFS
 */
object SecondDataCleaner {
  def main(args: Array[String]): Unit = {
    // 创建SparkSession
    val spark = SparkSession.builder()
      .appName("SecondDataCleaner")
      .master("local[2]")  // 本地运行，使用2个核心
      .getOrCreate()

    // 定义数据结构
    val schema = new StructType(Array(
      StructField("district", DataTypes.StringType),    // 市区
      StructField("community", DataTypes.StringType),   // 小区
      StructField("layout", DataTypes.StringType),      // 户型
      StructField("orientation", DataTypes.StringType), // 朝向
      StructField("floor", DataTypes.StringType),       // 楼层
      StructField("decoration", DataTypes.StringType),  // 装修情况
      StructField("elevator", DataTypes.StringType),    // 电梯
      StructField("area", DataTypes.DoubleType),        // 面积(㎡)
      StructField("price", DataTypes.DoubleType),       // 价格(万元)
      StructField("year", DataTypes.IntegerType)        // 年份
    ))

    // 读取原始数据
    val rawDataPath = "hdfs://master:9000/flume/events/Second_job/FlumeData.1750050648771"
    val df = spark.read
      .option("delimiter", ",")  // 假设数据是CSV格式，逗号分隔
      .schema(schema)
      .csv(rawDataPath)

    // 隐式转换
    import spark.implicits._
    // 数据清洗
    val cleanedDF = df
      // 过滤掉关键字段为空的记录
      .filter($"district".isNotNull && $"community".isNotNull)
      // 过滤掉面积、价格、年份为null或不合法的记录
      .filter($"area".isNotNull && $"area" > 0)  // 面积必须大于0
      .filter($"price".isNotNull && $"price" > 0) // 价格必须大于0
      .filter($"year".isNotNull && $"year" > 1980 && $"year" <= 2023) // 年份在合理范围内

    // 显示清洗后的数据摘要
    cleanedDF.printSchema()
    cleanedDF.show(10, truncate = false)

    // 保存清洗后的数据到HDFS
    val cleanDataPath = "hdfs://master:9000/flume/events/Second_data_analysis/clean"
    cleanedDF.write
      .mode("overwrite")  // 覆盖写入
      .format("orc")     // 使用ORC格式存储，压缩率高且适合分析
      .save(cleanDataPath)

    spark.stop()
  }
}