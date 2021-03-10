维度数据需要实现全量装载和增量更新操作的
1.全量装载:需要一次性的将全量的维度数据装载到redis中进行操作的。全量的批量数据更新操作。
2.维度数据的增量更新操作,可以使用增量更新数据使用canal更新操作redis中进行保存的。

// 需要实现业务管理实现和相关的业务实现操作。
kakfa的自定义的class信息可以进行实现业务的实现使用的，需要使用到protobuf进行业务实现操作和管理。

//fastjson处理操作的时候，需要关闭循环引用操作和依赖特性的。
//样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
// jedis的hset对应的存储的还是相关的字符串的信息的。
flink对应的使用的stream的环境信息如下:
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

