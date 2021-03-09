package cn.itcast.shop.realtime.etl.utils

import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties

/**
 * 封装kafka的实现类信息,完成kafka的属性配置操作。
 * */
object KafkaProps {

  /**
   * 获取kafka的属性信息
   * 返回封装好的kafka的配置信息。
   *
   * 作为一个消费者，需要配置的是反序列化的相关的配置的。
   * 对应的是包括key的反序列化以及value的反序列化的相关的序列化的配置的。
   * */
  def  getKafkaProps(): Properties ={
    val props=new Properties()
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,GlobalConfigUtil.`bootstrap.servers`)
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG,GlobalConfigUtil.`group.id`)
    //  客户端自动提交相关的kafka的消费信息。客户端每一次自动的提交相关的消费记录信息。
    props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,GlobalConfigUtil.`enable.auto.commit`)
    // 每次提交的间隔时间。
    props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,GlobalConfigUtil.`auto.commit.interval.ms`)
    // 设置每一次消费的是最新的数据信息。latest，还可以设置成为从起始位置进行消费操作的。
    props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,GlobalConfigUtil.`auto.offset.reset`)
    // 设置key的反序列化操作实现
    props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,GlobalConfigUtil.`key.deserializer`)
    //设置value的反序列化操作
    //props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,GlobalConfigUtil.`key.deserializer`)
    props
  }
}
