package cn.itcast.shop.realtime.etl.`trait`

import org.apache.flink.streaming.api.scala.DataStream

/**
 * 抽取特质类对象信息
 * 抽取所有etl操作公共的方法操作的
 * */
trait BaseEtl[T] {
  /**
   * 读取kafka数据的抽象的方法的信息的
   * */
  def  getKafkaDataStream(topic:String):DataStream[T]
  /**
   * 根据业务抽取出来对应的etl的操作的方法的。
   * */
  def  process(dataStream: DataStream[T])
}
