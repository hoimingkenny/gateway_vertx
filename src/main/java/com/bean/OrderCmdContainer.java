package com.bean;


import com.google.common.collect.Lists;
import com.thirdparty.order.OrderCmd;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class OrderCmdContainer {

    private static OrderCmdContainer ourInstance = new OrderCmdContainer();

    private OrderCmdContainer() {};

    public static OrderCmdContainer getInstance(){
        return  ourInstance;
    }

    /////////////////////////////// Use blocking queue

    private final BlockingQueue<OrderCmd> queue = new LinkedBlockingDeque<>();

    // add an element immediately without blocking
    public boolean cache(OrderCmd cmd){
        return queue.offer(cmd);
    }

    public int size(){
        return queue.size();
    }

    public List<OrderCmd> getAll(){
        List<OrderCmd> msgList = Lists.newArrayList();
        int count  = queue.drainTo(msgList);
        if(count == 0){
            return null;
        }else {
            return msgList;
        }
    }
}
