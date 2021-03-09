package cn.itcast.shop.realtime.etl.process

import cn.itcast.canal.bean.RowData
import cn.itcast.shop.realtime.etl.`trait`.MysqlBaseEtl
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

// 使用起来特别的不顺畅，在对象构造的时候使用参数，存在很大的问题。
case class SyncDimData(env: StreamExecutionEnvironment) extends MysqlBaseEtl(env){

}
