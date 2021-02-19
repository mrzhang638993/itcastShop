package cn.itcast.shop.realtime.etl.`trait`

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010

import java.util.Properties
/**
 * 完成mq中的字符串的编写操作实现
 * */
/**
 * 消费日志数据，购物车数据，以及评论数据的数据信息
 * 在kafka中存储的是string类型的数值的。
 * */
class MQBaseEtl extends BaseEtl[String]{
  /**
   * 读取kafka数据的抽象的方法的信息的
   * */
  override def getKafkaDataStream(topic: String): DataStream[String] ={
     // 创建消费者对象，从kafka中消费数据信息。
    val props=new Properties()
    //  配置consumer相关的消费者的对象的信息的。
    val consumer = new FlinkKafkaConsumer010[String](topic,new SimpleStringSchema(),props)
    null
  }

  /**
   * 根据业务抽取出来对应的etl的操作的方法的。
   * */
  override def process(dataStream: DataStream[String]): Unit = {

  }
}
