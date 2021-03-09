package cn.itcast.shop.realtime.etl.`trait`

import cn.itcast.canal.bean.RowData
import cn.itcast.shop.realtime.etl.utils.{GlobalConfigUtil, KafkaProps, RowDataDeserialzerSchema}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
//导入隐式转换操作。
import org.apache.flink.api.scala._
/**
 * 解析和处理mysql的binlog的数据信息
 *
 * canal进行的是增量数据的同步操作和实现的。
 * 不能进行全量数据的同步操作的。同时维度数据还存在增量数据的同步的操作的。
 * 增加数据同步采用的是canal的程序的。canal解析数据到kafka的，使用flink消费数据写入到redis中的
 * 全量数据的同步需要使用到
 * */
abstract  class MysqlBaseEtl(env:StreamExecutionEnvironment)  extends  BaseEtl[RowData]{
  /**
   * 读取kafka数据的抽象的方法的信息的
   * 需要将二进制的字节码对象转化成为对象数据信息进行转换操作的。
   * kafka的对象处理需要使用到protobuf进行操作的。可以快速的提高相关的数据的处理效率的。
   * */
  override def getKafkaDataStream(topic: String=GlobalConfigUtil.`input.topic.canal`): DataStream[RowData] = {
    // 创建消费者对象，从kafka中消费数据信息。
    val props=KafkaProps.getKafkaProps()
    //  配置consumer相关的消费者的对象的信息的。
    val consumer = new FlinkKafkaConsumer010[RowData](topic,new RowDataDeserialzerSchema(),props)
    // 将消费者consumer增加到消费者的数据源信息中进行管理操作。
    val value: DataStream[RowData] = env.addSource(consumer)
    value
  }
}
