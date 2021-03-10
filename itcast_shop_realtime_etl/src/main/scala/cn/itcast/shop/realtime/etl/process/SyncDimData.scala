package cn.itcast.shop.realtime.etl.process

import cn.itcast.canal.bean.RowData
import cn.itcast.shop.realtime.etl.`trait`.MysqlBaseEtl
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

// 使用起来特别的不顺畅，在对象构造的时候使用参数，存在很大的问题。
case class SyncDimData(env: StreamExecutionEnvironment) extends MysqlBaseEtl(env){
  /**
   * 抽取数据信息。
   * 根据业务抽取出来对应的etl的操作的方法的。实际的处理操作的方法，对应的是相关的etl的操作方法的。
   * */
  override def process(dataStream: DataStream[RowData]): Unit = ???
}
