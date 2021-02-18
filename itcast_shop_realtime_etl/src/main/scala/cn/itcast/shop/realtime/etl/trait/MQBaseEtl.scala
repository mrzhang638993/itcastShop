package cn.itcast.shop.realtime.etl.`trait`

import  org.apache.flink.streaming.api.scala.DataStream
/**
 * 完成mq中的字符串的编写操作实现
 * */
/**
 * 消费日志数据，购物车数据，以及评论数据的数据信息
 * */
class MQBaseEtl extends BaseEtl[String]{
  /**
   * 读取kafka数据的抽象的方法的信息的
   * */
  override def getKafkaDataStream(topic: String): DataStream[String] ={
    “”
  }

  /**
   * 根据业务抽取出来对应的etl的操作的方法的。
   * */
  override def process(dataStream: DataStream[String]): Unit = {

  }
}
