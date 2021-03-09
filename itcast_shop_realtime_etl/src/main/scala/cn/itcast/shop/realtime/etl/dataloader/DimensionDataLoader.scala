package cn.itcast.shop.realtime.etl.dataloader

import cn.itcast.shop.realtime.etl.bean.{DimGoodsCatDBEntity, DimGoodsDBEntity, DimOrgDBEntity, DimShopCatDBEntity, DimShopsDBEntity}
import cn.itcast.shop.realtime.etl.utils.{GlobalConfigUtil, RedisUtil}
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import redis.clients.jedis.Jedis

import java.sql.{Connection, DriverManager, ResultSet, SQLException, Statement}

/**
 * 数据装载类对象信息,全量装载数据到redis中进行操作实现。
 * 需要将下面的数据表对象装载到redis中进行操作的。
 * 1）商品维度表
 * 2）商品分类维度表
 * 3）店铺表
 * 4）组织机构表
 * 5）门店商品分类表
 * */
object DimensionDataLoader {
   /**
    * 维度表的数据全量装载到redis的数据库中进行存储。
    * */
  def main(args: Array[String]): Unit = {
    //  创建mysql的数据库连接信息
    Class.forName("com.mysql.jdbc.driver")
    // 创建数据库的连接信息,对应的体现的是相关的url信息。获取数据库的数据连接信息
    val conn: Connection = DriverManager.getConnection(
      s"jdbc:mysql://${GlobalConfigUtil.`mysql.server.ip`} : ${GlobalConfigUtil.`mysql.server.port`} /${GlobalConfigUtil.`mysql.server.database`}&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false",
      GlobalConfigUtil.`mysql.server.username`,
      GlobalConfigUtil.`mysql.server.password`)
    //  创建redis连接数据信息
    val jedis: Jedis = RedisUtil.getJedis()
    //  redis中默认的是存在16个数据库的。默认的是存在16个数据库的。保存到第一个数据库中的。
    jedis.select(1) //  加载第一个数据库中的数据信息的。
    //  加载维度表的数据到redis数据库中进行操作。
    //  装载商品维度表的数据信息。
    loadGoodDims(conn,jedis)
    loadDimShops(conn,jedis)
    loadDimGoodsCats(conn,jedis)
    loadDimOrg(conn,jedis)
    LoadDimShopCats(conn,jedis)
  }


  /**
   * 加载商品维度表的数据信息
   * 对应的执行全量加载数据的话,数据量一般的是不大的,是可以执行操作的。
   * */
  def  loadGoodDims(conn:Connection,jedis:Jedis): Unit ={
    var statement:Statement=null
    var set: ResultSet=null
    try {
      // 定义sql的查询语句操作
      val sql = """select goodsId,goodsName,shopId,goodsCatId,shopPrice from""".stripMargin
      //  创建statement
      statement = conn.createStatement()
      set = statement.executeQuery(sql)
      // 遍历数据集的数据信息。
      while (set.next()) { //  next方法判断元素是否存在后续的元素信息的
        val goodsId: Long = set.getLong("goodsId")
        val goodsName: String = set.getString("goodsName")
        val shopId: Long = set.getLong("shopId")
        val goodsCatId: Int = set.getInt("goodsCatId")
        val shopPrice: Double = set.getDouble("shopPrice")
        //  需要将获取到的数据库的数据写入到redis的数据库中，对应的需要配置相关的key和value的信息的。
        //  需要将5个字段封装成为一个json的结构，保存到redis的value中的。
        val value: DimGoodsDBEntity = DimGoodsDBEntity(goodsId, goodsName, shopId, goodsCatId, shopPrice)
        //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
        val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
        // jedis的hset对应的存储的还是相关的字符串的信息的。
        // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
        jedis.hset("itcast_shop:dim_goods", goodsId.toString, str)
      }

    }catch {
      case ex:SQLException=>{
          System.out.println(ex.getMessage)
      }
      case _=>{
        System.out.println("未知异常信息,难以补货")
      }
    }finally {
      if(set!=null){
        set.close()
      }
      if(statement!=null){
        statement.close()
      }
    }
  }

  // 其他维度的数据也需要执行操作
  /**
   * 店铺维度表的数据信息
   * */
  def  loadDimShops(conn:Connection,jedis:Jedis): Unit ={
    var statement:Statement=null
    var set: ResultSet=null
    try {
      // 定义sql的查询语句操作
      val sql = """select shopId,areaId,shopName,shopCompany from itcast_shops""".stripMargin
      //  创建statement
      statement = conn.createStatement()
      set = statement.executeQuery(sql)
      // 遍历数据集的数据信息。
      while (set.next()) { //  next方法判断元素是否存在后续的元素信息的
        val shopId = set.getInt("shopId")
        val areaId = set.getInt("areaId")
        val shopName = set.getString("shopName")
        val shopCompany = set.getString("shopCompany")
        //  需要将获取到的数据库的数据写入到redis的数据库中，对应的需要配置相关的key和value的信息的。
        //  需要将5个字段封装成为一个json的结构，保存到redis的value中的。
        val value: DimShopsDBEntity = DimShopsDBEntity(shopId, areaId, shopName, shopCompany)
        //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
        val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
        // jedis的hset对应的存储的还是相关的字符串的信息的。
        // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
        jedis.hset("itcast_shop:dim_shops", shopId.toString, str)
      }
    }catch {
      case ex:SQLException=>{
        System.out.println(ex.getMessage)
      }
      case _=>{
        System.out.println("未知异常信息,难以补货")
      }
    }finally {
      if(set!=null){
        set.close()
      }
      if(statement!=null){
        statement.close()
      }
    }
  }

  /**
   * 商品分类维度数据信息
   * */
  def  loadDimGoodsCats(conn:Connection,jedis:Jedis): Unit ={
    var statement:Statement=null
    var set: ResultSet=null
    try {
      // 定义sql的查询语句操作
      val sql = """select catId,parentId,catName,cat_level from itcast_goods_cats""".stripMargin
      //  创建statement
      statement = conn.createStatement()
      set = statement.executeQuery(sql)
      // 遍历数据集的数据信息。
      while (set.next()) { //  next方法判断元素是否存在后续的元素信息的
        val catId = set.getString("catId")
        val parentId = set.getString("parentId")
        val catName = set.getString("catName")
        val catLevel = set.getString("cat_level")
        //  需要将获取到的数据库的数据写入到redis的数据库中，对应的需要配置相关的key和value的信息的。
        //  需要将5个字段封装成为一个json的结构，保存到redis的value中的。
        val value: DimGoodsCatDBEntity = DimGoodsCatDBEntity(catId, parentId, catName, catLevel)
        //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
        val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
        // jedis的hset对应的存储的还是相关的字符串的信息的。
        // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
        jedis.hset("itcast_shop:dim_goods_cats", catId, str)
      }
    }catch {
      case ex:SQLException=>{
        System.out.println(ex.getMessage)
      }
      case _=>{
        System.out.println("未知异常信息,难以补货")
      }
    }finally {
      if(set!=null){
        set.close()
      }
      if(statement!=null){
        statement.close()
      }
    }
  }

  /**
   * 商品维度
   * */
  def  loadDimOrg(conn:Connection,jedis:Jedis): Unit ={
    var statement:Statement=null
    var set: ResultSet=null
    try {
      // 定义sql的查询语句操作
      val sql = """select orgid,parentid,orgName,orgLevel from itcast_org""".stripMargin
      //  创建statement
      statement = conn.createStatement()
      set = statement.executeQuery(sql)
      // 遍历数据集的数据信息。
      while (set.next()) { //  next方法判断元素是否存在后续的元素信息的
        val orgId = set.getInt("orgId")
        val parentId = set.getInt("parentId")
        val orgName = set.getString("orgName")
        val orgLevel = set.getInt("orgLevel")
        //  需要将获取到的数据库的数据写入到redis的数据库中，对应的需要配置相关的key和value的信息的。
        //  需要将5个字段封装成为一个json的结构，保存到redis的value中的。
        val value: DimOrgDBEntity = DimOrgDBEntity(orgId, parentId, orgName, orgLevel)
        //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
        val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
        // jedis的hset对应的存储的还是相关的字符串的信息的。
        // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
        jedis.hset("itcast_shop:dim_org", orgId.toString, str)
      }
    }catch {
      case ex:SQLException=>{
        System.out.println(ex.getMessage)
      }
      case _=>{
        System.out.println("未知异常信息,难以补货")
      }
    }finally {
      if(set!=null){
        set.close()
      }
      if(statement!=null){
        statement.close()
      }
    }
  }

  def  LoadDimShopCats(conn:Connection,jedis:Jedis): Unit ={
    var statement:Statement=null
    var set: ResultSet=null
    try {
      // 定义sql的查询语句操作
      val sql = """select catId,parentId,catName,catSort from itcast_shop_cats""".stripMargin
      //  创建statement
      statement = conn.createStatement()
      set = statement.executeQuery(sql)
      // 遍历数据集的数据信息。
      while (set.next()) { //  next方法判断元素是否存在后续的元素信息的
        val catId = set.getString("catId")
        val parentId = set.getString("parentId")
        val catName = set.getString("catName")
        val catLevel = set.getString("catSort")
        //  需要将获取到的数据库的数据写入到redis的数据库中，对应的需要配置相关的key和value的信息的。
        //  需要将5个字段封装成为一个json的结构，保存到redis的value中的。
        val value: DimShopCatDBEntity = DimShopCatDBEntity(catId, parentId, catName, catLevel)
        //  样例类对象转化成为json字符串信息.转换操作的时候需要关系循环引用的信息的。
        val str: String = JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect)
        // jedis的hset对应的存储的还是相关的字符串的信息的。
        // jedis的key的组成，可以使用如下的方式：数据库名称:表的名称+主键信息。
        jedis.hset("itcast_shop:dim_shop_cats", catId.toString, str)
      }
    }catch {
      case ex:SQLException=>{
        System.out.println(ex.getMessage)
      }
      case _=>{
        System.out.println("未知异常信息,难以补货")
      }
    }finally {
      if(set!=null){
        set.close()
      }
      if(statement!=null){
        statement.close()
      }
    }
  }
}
