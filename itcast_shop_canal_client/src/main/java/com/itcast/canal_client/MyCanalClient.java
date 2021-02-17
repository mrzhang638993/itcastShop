package com.itcast.canal_client;

import cn.itcast.canal.bean.RowData;
import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.itcast.canal_client.kafka.KafkaSender;
import com.itcast.canal_client.util.ConfigUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义初始化的最初的工具类的信息
 *
 * */
public class MyCanalClient {

    /**
     * 引入日志框架，对应的完成相关的日志的操作和实现。
     * */
    private  CanalConnector canalConnector;

    /**
     * 构建kafka的消息发送者对象，完成相关的消息发送操作。
     * */
    private KafkaSender kafkaSender;

    /**
     * 定义自己的构造器和对应的方法实现
     * */
    public  MyCanalClient(){
         //  初始化客户端的连接操作的,对应的创建相关的客户端的canal的连接的。
         canalConnector= CanalConnectors.
                newClusterConnector(ConfigUtil.zookeeperServerIp(),
                ConfigUtil.canalServerDestination()
                ,ConfigUtil.canalServerUsername(),
                ConfigUtil.canalServerPassword());
        kafkaSender=new KafkaSender();
    }

    public   void  start(){
         try {
             // 连接canal的服务器操作
             canalConnector.connect();
             // 自动回滚上一次未成功的处理
             canalConnector.rollback();
             // 订阅对应的信息,订阅指定的canal服务实例对象
             canalConnector.subscribe(ConfigUtil.canalSubscribeFilter());
             //  不停的循环获取数据进行消费数据信息
             while (true){
                 // 开始批次消费对应的canal的数据信息
                 Message message = canalConnector.getWithoutAck(Integer.parseInt(ConfigUtil.kafkaBatch_size_config()));
                 // 获取批次号id,对应的实现相关的批次数据的提交操作和管理实现。
                 long id = message.getId();
                 // 获取需要处理的数据集合信息
                 List<CanalEntry.Entry> entries = message.getEntries();
                 //  没有拉取到对应的数据，就不用管理相关的数据信息了。
                 if (entries.size()==0||entries.size()==-1){
                     //  表示没有获取到数据
                     continue;
                 }
                 // 获取到数据,开始数据的解析操作。
                 Map binlogMsgMap = binlogMessageToMap(message);
                 RowData rowData = new RowData(binlogMsgMap);
                 System.out.println(rowData.toString());
                 if (binlogMsgMap.size() > 0) {
                     kafkaSender.send(rowData);
                 }
                 canalConnector.ack(id);
             }
         }catch (CanalClientException | InvalidProtocolBufferException canalClientException){
             // 出现异常的话，释放对应的canal连接信息
             // 打印日志操作和输出信息。
         }finally {
             canalConnector.disconnect();
         }
    }

    /**
     * 将binlog日志转换为Map结构
     * @param message
     * @return
     */
    private Map binlogMessageToMap(Message message) throws InvalidProtocolBufferException {
        Map rowDataMap = new HashMap();

        // 1. 遍历message中的所有binlog实体
        for (CanalEntry.Entry entry : message.getEntries()) {
            // 只处理事务型binlog
            if(entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN ||
                    entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            // 获取binlog文件名
            String logfileName = entry.getHeader().getLogfileName();
            // 获取logfile的偏移量
            long logfileOffset = entry.getHeader().getLogfileOffset();
            // 获取sql语句执行时间戳
            long executeTime = entry.getHeader().getExecuteTime();
            // 获取数据库名
            String schemaName = entry.getHeader().getSchemaName();
            // 获取表名
            String tableName = entry.getHeader().getTableName();
            // 获取事件类型 insert/update/delete
            String eventType = entry.getHeader().getEventType().toString().toLowerCase();

            rowDataMap.put("logfileName", logfileName);
            rowDataMap.put("logfileOffset", logfileOffset);
            rowDataMap.put("executeTime", executeTime);
            rowDataMap.put("schemaName", schemaName);
            rowDataMap.put("tableName", tableName);
            rowDataMap.put("eventType", eventType);

            // 获取所有行上的变更
            Map<String, String> columnDataMap = new HashMap<>();
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            List<CanalEntry.RowData> columnDataList = rowChange.getRowDatasList();
            for (CanalEntry.RowData rowData : columnDataList) {
                if(eventType.equals("insert") || eventType.equals("update")) {
                    for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
                        columnDataMap.put(column.getName(), column.getValue().toString());
                    }
                }
                else if(eventType.equals("delete")) {
                    for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
                        columnDataMap.put(column.getName(), column.getValue().toString());
                    }
                }
            }

            rowDataMap.put("columns", columnDataMap);
        }

        return rowDataMap;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
