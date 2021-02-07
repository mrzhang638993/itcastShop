package com.itcast.canal_client;

import com.alibaba.otter.canal.client.CanalConnectors;

/**
 * 程序入口类
 */
public class Entrance {
    public static void main(String[] args) {
        //实例化对象，调用start方法拉取canalserver端的数据
        CanalClient client = new CanalClient();
        client.start();
    }
}
