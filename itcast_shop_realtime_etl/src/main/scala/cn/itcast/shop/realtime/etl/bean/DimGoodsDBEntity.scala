package cn.itcast.shop.realtime.etl.bean

import com.alibaba.fastjson.{JSON, JSONObject}

import scala.beans.BeanProperty

/**
 * 商品维度样例类信息
 * 样例类对象的话，可以处理成为相关的bean对象信息的。
 *
 * @BeanProperty: 会生成相关的set以及get方法。
 * */
case class DimGoodsDBEntity(
                             @BeanProperty goodsId:Long=0,  //  商品id
                             @BeanProperty goodsName:String="", // 商品名称
                             @BeanProperty shopId:Long=0,   // 店铺id
                             @BeanProperty goodsCatId:Int=0,  // 店铺类别id
                             @BeanProperty shopPrice:Double=0.0  // 商品价格信息
                           )

/**
 * 样例类的半生对象
 * */
object DimGoodsDBEntity{
  /**
   * 样例类的半生对象信息.
   * 目标是实现将字符串解析成为样例类对象信息。
   * */
   def apply(json:String): DimGoodsDBEntity ={
      if(json!=null){
        val jsonObject: JSONObject = JSON.parseObject(json)
        new DimGoodsDBEntity(
          jsonObject.getLongValue("goodsId"),
          jsonObject.getString("goodsName"),
          jsonObject.getLong("shopId"),
          jsonObject.getInteger("goodsCatId"),
          jsonObject.getDouble("shopPrice")
        )
      }else{
        new DimGoodsDBEntity()
      }
   }
}

/**
 * 商品分类表的数据信息
 * */
// 商品分类维度样例类
case class DimGoodsCatDBEntity(@BeanProperty catId:String="",	// 商品分类id
                               @BeanProperty parentId:String="",	// 商品分类父id
                               @BeanProperty catName:String="",	// 商品分类名称
                               @BeanProperty cat_level:String="")	// 商品分类级别
/**
 * 商品分类表的半生对象
 * */
object DimGoodsCatDBEntity{

   def apply(json:String): DimGoodsCatDBEntity ={
     if(json!=null){
       val jsonObject: JSONObject = JSON.parseObject(json)
       new DimGoodsCatDBEntity(
         jsonObject.getString("catId"),
         jsonObject.getString("parentId"),
         jsonObject.getString("catName"),
         jsonObject.getString("cat_level")
       )
     }else{
       new DimGoodsCatDBEntity()
     }
   }
}

// 店铺维度样例类
case class DimShopsDBEntity(@BeanProperty shopId:Int=0,		// 店铺id
                            @BeanProperty areaId:Int=0,		// 店铺所属区域id
                            @BeanProperty shopName:String="",	// 店铺名称
                            @BeanProperty shopCompany:String="")	// 公司名称
/**
 * 店铺维度的半生对象信息
 * */
object DimShopsDBEntity{
  def apply(json:String):DimShopsDBEntity={
    if(json!=null){
      val jsonObject: JSONObject = JSON.parseObject(json)
      new DimShopsDBEntity(
        jsonObject.getInteger("shopId"),
        jsonObject.getInteger("areaId"),
        jsonObject.getString("shopName"),
        jsonObject.getString("shopCompany")
      )
    }else{
      new DimShopsDBEntity()
    }
  }
}

// 组织结构维度样例类
case class DimOrgDBEntity(@BeanProperty orgId:Int=0,			// 机构id
                          @BeanProperty parentId:Int=0,		// 机构父id
                          @BeanProperty orgName:String="",		// 组织机构名称
                          @BeanProperty orgLevel:Int=0)		// 组织机构级别
/**
 * 组织结构的数据信息
 * */
object DimOrgDBEntity{

   def apply(json:String):DimOrgDBEntity={
     if(json!=null){
       val jsonObject: JSONObject = JSON.parseObject(json)
       new DimOrgDBEntity(
         jsonObject.getInteger("orgId"),
         jsonObject.getInteger("parentId"),
         jsonObject.getString("orgName"),
         jsonObject.getInteger("orgLevel")
       )
     }else{
       new DimOrgDBEntity()
     }
   }
}

// 门店商品分类维度样例类
case class DimShopCatDBEntity(@BeanProperty catId:String = "",	 // 商品分类id
                              @BeanProperty parentId:String = "",// 商品分类父id
                              @BeanProperty catName:String = "", // 商品分类名称
                              @BeanProperty catSort:String = "")// 商品分类级别
/**
 * 门店的样例类信息
 * */
object DimShopCatDBEntity{
    def apply(json:String): DimShopCatDBEntity ={
      if(json!=null){
        val jsonObject: JSONObject = JSON.parseObject(json)
        new DimShopCatDBEntity(
          jsonObject.getString("catId"),
          jsonObject.getString("parentId"),
          jsonObject.getString("catName"),
          jsonObject.getString("catSort")
        )
      }else{
        new DimShopCatDBEntity()
      }
    }
}