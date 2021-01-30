package com.json.study.thread.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;

public class DisruptorTest_02 {

    public static void main(String[] args) {
        int bufferSize = 1024;
        Disruptor<MyEvent> disruptor = new Disruptor<>(MyEvent::new, bufferSize, Executors.defaultThreadFactory());
        disruptor.handleEventsWith(((myEvent, sequence, endOfBatch) -> System.out.println(myEvent)));
        disruptor.start();

        RingBuffer<MyEvent> ringBuffer = disruptor.getRingBuffer();
//        ringBuffer.publishEvent((myEvent, sequence) -> myEvent.setValue(22222L));
        ringBuffer.publishEvent(((myEvent, l, aLong) -> myEvent.setValue(aLong)),333333L);

    }
}
