package zj.producer;

import java.util.concurrent.Semaphore;

public class Product {
    private static Integer count = 0;  // 产品初始数量
    // 创建三个信号量
    // 资源信号量
    final Semaphore empty = new Semaphore(10); // 箱子最多放10个产品
    final Semaphore full = new Semaphore(0); // 箱子为空
    // 互斥信号量
    final Semaphore mutex = new Semaphore(1);   // 缓冲池互斥信号量

    public static void main(String[] args) {
        Product test1 = new Product();
        new Thread(test1.new Producer()).start();
        new Thread(test1.new Consumer()).start();
        new Thread(test1.new Producer()).start();
        new Thread(test1.new Consumer()).start();
    }

    // 生产者生产产品
    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000); // 等待1s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    empty.acquire(); // 申请空缓冲区资源
                    mutex.acquire(); // 申请缓冲池的使用
                    count++;   // 生产者生产产品
                    System.out.println(Thread.currentThread().getName()+ "生产者生产，目前总共有" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release(); // 释放缓冲池的使用权
                    full.release();  // 释放满缓冲区
                }
            }
        }
    }

    //消费者消费苹果
    class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000); // 等待1s
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    full.acquire();  // 申请满缓冲区资源
                    mutex.acquire(); // 申请缓冲池的使用权
                    count--;  // 消费者拿走一个产品
                    System.out.println(Thread.currentThread().getName()+ "消费者消费，目前总共有" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release(); // 释放缓冲池的使用权
                    empty.release(); // 释放空缓冲区
                }
            }
        }
    }
}
