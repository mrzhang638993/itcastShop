package cn.itcast.shop.realtime.etl.process

import cn.itcast.canal.bean.RowData
import cn.itcast.shop.realtime.etl.`trait`.MysqlBaseEtl
import cn.itcast.shop.realtime.etl.bean.{DimGoodsCatDBEntity, DimGoodsDBEntity, DimOrgDBEntity, DimShopCatDBEntity, DimShopsDBEntity}
import cn.itcast.shop.realtime.etl.utils.RedisUtil
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import redis.clients.jedis.Jedis

// 使用起来特别的不顺畅，在对象构造的时候使用参数，存在很大的问题。
case class SyncDimData(env: StreamExecutionEnvironment) extends MysqlBaseEtl(env){
  /**
   * 抽取数据信息。
   * 根据业务抽取出来对应的etl的操作的方法的。实际的处理操作的方法，对应的是相关的etl的操作方法的。
   *
   * 增量更新数据到redis中。
   * */
  override def process(): Unit = {
    /**
     * 1.获取数据源
     * 2.过滤出来相关的维度表
     * 3.处理同步过来的数据，更新到redis中。
     *
     * */
      //获取kafka的基类数据信息。对于stream进行操作的话，不能使用for等循环遍历操作实现的。
    val value: DataStream[RowData] = getKafkaDataStream()
      //过滤得到维度表的数据
    val dimRowDataStream: DataStream[RowData] = value.filter {
      //使用模式匹配原则匹配表的名称进行操作
      rowdata =>
        rowdata.getTableName match {
          case "itcast_goods" => true
          case "itcast_shops" => true
          case "itcast_goods_cats" => true
          case "itcast_org" => true
          case "itcast_shop_cats" => true
          //需要增加else操作的，否则后续的会出现各种问题的。
          case _ => false
        }
    }
    //  处理得到的干净的数据执行数据的操作实现.实现数据的下沉操作实现
    dimRowDataStream.addSink(new RichSinkFunction[RowData]{
      var jedis:Jedis=_
      /**
       * 只是会调用一次的,用于开启数据源
       * */
      override def open(parameters: Configuration): Unit ={
          jedis=RedisUtil.getJedis()
          //  维度数据处于第一个数据库中的，需要指定redis对应的数据库执行操作的。
          jedis.select(1)
      }
      /**
       * 只会调用一次,用于关闭数据源执行数据源的关闭操作
       * */
      override def close(): Unit = {
        //  处于连接状态的话，关闭连接信息。
        if(jedis.isConnected) {
          jedis.close()
        }
      }

      /**
       * 更新维度表的数据信息。
       * redis的更新和删除是一样的操作的。不需要区分相关的insert还是update操作的。
       * */
      def updateDimData(rowData: RowData): Unit = {
               // 执行更新操作实现,根据不同的维度表的数据执行维度表的数据的更新操作实现。
             rowData.getTableName match {
               case "itcast_goods"=>{
                 //  选择和处理相关的数据
                 val goodsId: Long = rowData.getColumns.get("goodsId").toLong
                 val goodsName: String = rowData.getColumns.get("goodsName")
                 val shopId: Long = rowData.getColumns.get("shopId").toLong
                 val goodsCatId: Int = rowData.getColumns.get("goodsCatId").toInt
                 val shopPrice: Double = rowData.getColumns.get("shopPrice").toDouble
                 val value: DimGoodsDBEntity = DimGoodsDBEntity(goodsId, goodsName, shopId, goodsCatId, shopPrice)
                 //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
                 val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
                 // jedis的hset对应的存储的还是相关的字符串的信息的。
                 // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
                 jedis.hset("itcast_shop:dim_goods", goodsId.toString, str)
               }
               case "itcast_shops"=>{
                 val shopId = rowData.getColumns.get("shopId").toInt
                 val areaId = rowData.getColumns.get("areaId").toInt
                 val shopName =rowData.getColumns.get("shopName")
                 val shopCompany =rowData.getColumns.get("shopCompany")
                 //  需要将获取到的数据库的数据写入到redis的数据库中，对应的需要配置相关的key和value的信息的。
                 //  需要将5个字段封装成为一个json的结构，保存到redis的value中的。
                 val value: DimShopsDBEntity = DimShopsDBEntity(shopId, areaId, shopName, shopCompany)
                 //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
                 val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
                 // jedis的hset对应的存储的还是相关的字符串的信息的。
                 // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
                 jedis.hset("itcast_shop:dim_shops", shopId.toString, str)
               }
               case "itcast_goods_cats"=>{
                 val catId = rowData.getColumns.get("catId")
                 val parentId = rowData.getColumns.get("parentId")
                 val catName = rowData.getColumns.get("catName")
                 val catLevel = rowData.getColumns.get("cat_level")
                 //  需要将获取到的数据库的数据写入到redis的数据库中，对应的需要配置相关的key和value的信息的。
                 //  需要将5个字段封装成为一个json的结构，保存到redis的value中的。
                 val value: DimGoodsCatDBEntity = DimGoodsCatDBEntity(catId, parentId, catName, catLevel)
                 //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
                 val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
                 // jedis的hset对应的存储的还是相关的字符串的信息的。
                 // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
                 jedis.hset("itcast_shop:dim_goods_cats", catId, str)
               }
               case "itcast_org"=>{
                 val orgId = rowData.getColumns.get("orgId").toInt
                 val parentId = rowData.getColumns.get("parentId").toInt
                 val orgName = rowData.getColumns.get("orgName")
                 val orgLevel = rowData.getColumns.get("orgLevel").toInt
                 //  需要将获取到的数据库的数据写入到redis的数据库中，对应的需要配置相关的key和value的信息的。
                 //  需要将5个字段封装成为一个json的结构，保存到redis的value中的。
                 val value: DimOrgDBEntity = DimOrgDBEntity(orgId, parentId, orgName, orgLevel)
                 //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
                 val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
                 // jedis的hset对应的存储的还是相关的字符串的信息的。
                 // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
                 jedis.hset("itcast_shop:dim_org", orgId.toString, str)
               }
               case  "itcast_shop_cats"=>{
                 val catId = rowData.getColumns.get("catId")
                 val parentId = rowData.getColumns.get("parentId")
                 val catName = rowData.getColumns.get("catName")
                 val catLevel = rowData.getColumns.get("catSort")
                 //  需要将获取到的数据库的数据写入到redis的数据库中，对应的需要配置相关的key和value的信息的。
                 //  需要将5个字段封装成为一个json的结构，保存到redis的value中的。
                 val value: DimShopCatDBEntity = DimShopCatDBEntity(catId, parentId, catName, catLevel)
                 //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
                 val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
                 // jedis的hset对应的存储的还是相关的字符串的信息的。
                 // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
                 jedis.hset("itcast_shop:dim_shop_cats", catId.toString, str)
               }
               case _=>
             }
      }

      /**
       * 删除维度表的数据信息。
       * */
      def deleteDimData(rowData: RowData): Unit = {
        rowData.getTableName match {
          case "itcast_goods"=>{
              jedis.hdel("itcast_goods:dim_goods",rowData.getColumns.get("goodsId"))
          }
          case "itcast_shops"=>{
             jedis.hdel("itcast_goods:dim_shops",rowData.getColumns.get("shopId"))
          }
          case "itcast_goods_cats"=>{
            jedis.hdel("itcast_goods:dim_goods_cats",rowData.getColumns.get("catId"))
          }
          case "itcast_org"=>{
            jedis.hdel("itcast_goods:dim_org",rowData.getColumns.get("orgId"))
          }
          case  "itcast_shop_cats"=>{
            jedis.hdel("itcast_goods:dim_shop_cats",rowData.getColumns.get("catId"))
          }
          case _=>
        }
      }

      /**
       * 真正的业务实现类的处理逻辑,实现业务处理操作。
       * */
      override def invoke(rowData: RowData, context: SinkFunction.Context[_]): Unit = {
        rowData.getEventType match {
          case eventType if(eventType=="INSERT"||eventType=="UPDATE")=>updateDimData(rowData)
          case "DELETE"=>deleteDimData(rowData)
          //  忽略其他的异常场景信息。
          case _=>
        }
      }
    })

  }
}
