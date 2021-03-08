package cn.itcast.shop.realtime.etl.`trait`

import cn.itcast.shop.realtime.etl.utils.KafkaProps
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
//导入隐式转换操作。
import org.apache.flink.api.scala._
/**
 * 完成mq中的字符串的编写操作实现
 * */
/**
 * 消费日志数据，购物车数据，以及评论数据的数据信息
 * 在kafka中存储的是string类型的数值的。
 * */
abstract  class MQBaseEtl(env:StreamExecutionEnvironment) extends BaseEtl[String]{
  /**
   * 读取kafka数据的抽象的方法的信息的
   * */
  override def getKafkaDataStream(topic: String): DataStream[String] ={
     // 创建消费者对象，从kafka中消费数据信息。
    val props=KafkaProps.getKafkaProps()
    //  配置consumer相关的消费者的对象的信息的。
    val consumer = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),props)
    // 将消费者consumer增加到消费者的数据源信息中进行管理操作。
    val value: DataStream[String] = env.addSource(consumer)
    value
  }
}
