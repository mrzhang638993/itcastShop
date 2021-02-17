package com.itcast.canal_client.kafka;

import cn.itcast.canal.bean.RowData;
import com.itcast.canal_client.util.ConfigUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 自定义实现相关的kafka的sender的机制。
 * */
public class MyKafkaSender {
    /**
     * 存储相关的kafka的属性信息
     * */
    private Properties kafkaProperties=new Properties();
    /**
     * 存储相关的kafkaproducer的实现信息
     * */
    private KafkaProducer<String, RowData> kafkaProducer;
    /**
     * MyKafkaSender的构造器的相关的方法实现
     * */
    public MyKafkaSender(){
        // 构造类对象
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ConfigUtil.kafkaBootstrap_servers_config());
        kafkaProperties.put(ProducerConfig.BATCH_SIZE_CONFIG,ConfigUtil.kafkaBatch_size_config());
        kafkaProperties.put(ProducerConfig.ACKS_CONFIG,ConfigUtil.kafkaAcks());
        kafkaProperties.put(ProducerConfig.RETRIES_CONFIG,ConfigUtil.kafkaRetries());
        kafkaProperties.put(ProducerConfig.CLIENT_ID_CONFIG,ConfigUtil.kafkaClient_id_config());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,ConfigUtil.kafkaKey_serializer_class_config());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,ConfigUtil.kafkaValue_serializer_class_config());
        //  实例化对应的生产者对象
        kafkaProducer=new KafkaProducer<String, RowData>(kafkaProperties);
    }

    /**
     * 输入写入到kafka集群中进行管理操作和实现
     * */
    public void send(RowData rowData){
       // 数据写入到kafka集群中进行管理操作。
        kafkaProducer.send(new ProducerRecord<>(ConfigUtil.kafkaTopic(),rowData));
    }
}
