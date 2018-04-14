package com.wangtianya.yaa.net.ping2;

import com.wangtianya.yaa.net.ping2.impl.PingTaskImpl;
import com.wangtianya.yaa.net.ping2.inteface.PingListener;
import com.wangtianya.yaa.net.ping2.inteface.PingTask;

/**
 * 如果有一天和人协作，再换成抽象工厂类。自己用，不要过度去整了。
 */
public class PingTaskFactory {

    public static PingTask newOne(String host, PingListener listener) {
        PingTask pingTask = new PingTaskImpl();
        pingTask.setHost(host);
        pingTask.setListener(listener);
        return pingTask;
    }

    public static PingTask newOne(String host, int times, PingListener listener) {
        PingTask pingTask = new PingTaskImpl();
        pingTask.setHost(host);
        pingTask.setTimes(times);
        pingTask.setListener(listener);
        return pingTask;
    }
}