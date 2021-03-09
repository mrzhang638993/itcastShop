package cn.itcast.shop.realtime.etl.utils

import cn.itcast.canal.bean.RowData
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema

/**
 * 特定类的序列化的方式
 * 定义一个反序列化的代码的实现方式和操作的。
 * */
class RowDataDeserialzerSchema  extends  AbstractDeserializationSchema[RowData]{
  // 实现数据的反序列化操作。
  override def deserialize(bytes: Array[Byte]): RowData = {
    // 解析protobuf的数据，底层调用的是val rowData: CanalModel.RowData = CanalModel.RowData.parseFrom(bytes)
    val rowData=new RowData(bytes)
    rowData
  }
}
