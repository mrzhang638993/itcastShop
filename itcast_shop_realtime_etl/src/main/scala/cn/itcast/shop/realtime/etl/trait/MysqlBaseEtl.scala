package cn.itcast.shop.realtime.etl.`trait`

import cn.itcast.canal.bean.RowData
import  org.apache.flink.streaming.api.scala.DataStream

/**
 * 解析和处理mysql的binlog的数据信息
 *
 * canal进行的是增量数据的同步操作和实现的。
 * 不能进行全量数据的同步操作的。同时维度数据还存在增量数据的同步的操作的。
 * 增加数据同步采用的是canal的程序的。canal解析数据到kafka的，使用flink消费数据写入到redis中的
 * 全量数据的同步需要使用到
 * */
class MysqlBaseEtl  extends  BaseEtl[RowData]{
  /**
   * 读取kafka数据的抽象的方法的信息的
   * 需要将二进制的字节码对象转化成为对象数据信息进行转换操作的。
   * kafka的对象处理需要使用到protobuf进行操作的。可以快速的提高相关的数据的处理效率的。
   * */
  override def getKafkaDataStream(topic: String): DataStream[RowData] = {
      null
  }

  /**
   * 根据业务抽取出来对应的etl的操作的方法的。
   * */
  override def process(dataStream: DataStream[RowData]): Unit = {
      null
  }
}
