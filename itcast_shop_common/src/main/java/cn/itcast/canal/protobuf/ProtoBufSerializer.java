package cn.itcast.canal.protobuf;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/***
 * 用于kafka消息序列化的类
 * 自定义实现序列化的方式实现相关的更多的操作和配置实现管理。
 * 自由继承ProtoBufable的类对象才可以实现序列化的方式操作的。
 */
public class ProtoBufSerializer implements Serializer<ProtoBufable> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // 需要重写相关的序列话的方式和方法操作的。采用默认的序列化编码的方式实现操作。
    }

    @Override
    public byte[] serialize(String topic, ProtoBufable data) {
        // 需要对应的ProtoBufable的对象实现自定义的序列化方式的。实现toByte的方法的。
        return data.toByte();
    }

    @Override
    public void close() {
    }
}
