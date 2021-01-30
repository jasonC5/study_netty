package com.json.study.thread.disruptor;


import com.lmax.disruptor.EventHandler;

public class MyEventHandler implements EventHandler<MyEvent> {
    public static volatile long count = 0L;

    /**
     *
     * @param myEvent 事件
     * @param sequence 序号
     * @param endOfBatch 是否为最后一个元素
     * @throws Exception
     */
    @Override
    public void onEvent(MyEvent myEvent, long sequence, boolean endOfBatch) throws Exception {
        count++;
        System.out.println("【"+Thread.currentThread().getName() + "】_"+ myEvent.toString() + " sequence:"+sequence);
    }
}
