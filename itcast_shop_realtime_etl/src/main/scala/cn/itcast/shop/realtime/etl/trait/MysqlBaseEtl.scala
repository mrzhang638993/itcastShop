package cn.itcast.shop.realtime.etl.`trait`

import cn.itcast.canal.bean.RowData
import  org.apache.flink.streaming.api.scala.DataStream

/**
 * 解析和处理mysql的binlog的数据信息
 * */
class MysqlBaseEtl  extends  BaseEtl[RowData]{
  /**
   * 读取kafka数据的抽象的方法的信息的
   * */
  override def getKafkaDataStream(topic: String): DataStream[RowData] = ???

  /**
   * 根据业务抽取出来对应的etl的操作的方法的。
   * */
  override def process(dataStream: DataStream[RowData]): Unit = ???
}
