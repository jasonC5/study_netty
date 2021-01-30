package com.json.study.thread.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;

public class DisruptorTest_01 {

    public static void main(String[] args) {
        MyEventFactory myEventFactory = new MyEventFactory();
        int  bufferSize = 1024;// 2的N次方
        //线程工程：产生消费者
        Disruptor<MyEvent> myEventDisruptor = new Disruptor<>(myEventFactory, bufferSize, Executors.defaultThreadFactory());

        myEventDisruptor.handleEventsWith(new MyEventHandler());
        myEventDisruptor.start();

        RingBuffer<MyEvent> ringBuffer = myEventDisruptor.getRingBuffer();
        //官方示例
        long sequence = ringBuffer.next();
        try{

            MyEvent myEvent = ringBuffer.get(sequence);
            myEvent.setValue(1111111111L);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ringBuffer.publish(sequence);
        }

    }
}
