package com.json.study.thread.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.*;

public class DisruptorTest_03 {

    public static void main(String[] args) throws InterruptedException {
        int bufferSize = 1024;
        Disruptor<MyEvent> disruptor = new Disruptor<>(new MyEventFactory(), bufferSize, Executors.defaultThreadFactory(),
                ProducerType.MULTI, new BlockingWaitStrategy());//ProducerType.SINGLE  单线程  不加锁 更快
        disruptor.handleEventsWith(new MyEventHandler());
        disruptor.start();
        RingBuffer<MyEvent> ringBuffer = disruptor.getRingBuffer();
        //-----------------------------------------------
        Integer threadCount = 50;
        CyclicBarrier barrier = new CyclicBarrier(threadCount);//
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            final long threadNum = i;
            executorService.submit(()-> {
                System.out.println("Thread ready to start ___"+threadNum);
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 100; j++) {
                    ringBuffer.publishEvent(((myEvent, l) -> {
                        System.out.println("------生产了"+threadNum);
                        myEvent.setValue(threadNum);
                    }));
                }
            });
        }
        executorService.shutdown();
        TimeUnit.SECONDS.sleep(3);
        System.out.println(MyEventHandler.count);
    }

}
