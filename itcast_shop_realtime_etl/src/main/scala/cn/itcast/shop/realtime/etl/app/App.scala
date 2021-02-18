package cn.itcast.shop.realtime.etl.app

import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.{CheckpointConfig, StreamExecutionEnvironment}

/***
 * 创建etl实时处理模块，进行etl实时处理操作
 */
object App {

   /**
    * 程序启动入口程序
    * */
  def main(args: Array[String]): Unit = {
      /**
       * 1.设置flink的流式处理环境；
       * 2.设置flink的并行度，测试环境下面设置并行度为1
       * flink的并行度的选择，最好是在使用作业提交的时候，指定并行度执行操作的。不要在代码中直接指定并行度执行操作的
       * 3.开启flink的checkpoint增加容错支持操作
       * 4.接入kafka数据源，消费kafka的数据信息
       * 5.实现所有的etl业务操作，执行任务操作实现。
       * */
    //  TODO 设置flink的流式处理环境；
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //  设置并行度,方便观察操作
    env.setParallelism(1)
    // 设置checkpoint的容错机制操作。单位是毫秒的机制的。每5秒执行一次checkpoint操作
    env.enableCheckpointing(5000L,CheckpointingMode.EXACTLY_ONCE)
    // 当作业被取消的时候，保留以前的checkpoint信息。避免数据的丢失操作
    env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
    // 设置同一个时间存在多少个检查点的。检查点的操作是否可以并行操作的。当前的并行度为1，不需要并行操作的
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    //  设置flink的重启策略。默认的是不停的重启操作的。
    // 设置重启次数为5，固定的重启策略，每次重启，延迟5秒钟。5次失败之后，job运行失败
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(5,5000))
    // checkpoint的HDFS保存位置
    env.setStateBackend(new FsStateBackend("hdfs://hadoop01:8020/flink/checkpoint/"))
    // 配置两次checkpoint的最小时间间隔
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(1000)
    // 配置最大checkpoint的并行度
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    // 配置checkpoint的超时时长
    env.getCheckpointConfig.setCheckpointTimeout(60000)
  }
}
