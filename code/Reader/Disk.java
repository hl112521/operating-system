package zj.reader;

import java.util.concurrent.Semaphore;

public class Disk implements Runnable {
    private String data_str;    // 读写数据
    private int readercount;   // 读者数量
    // 互斥信号量
    private Semaphore wmutex;  // 读写互斥信号量
    private Semaphore read_count_mutex; // 修改读者数量的互斥信号量
    // 资源信号量
    private Semaphore rmutex;   // 最多读者数目资源信号量

    Disk() {
        // 初始化信号量
        wmutex = new Semaphore(1);
        rmutex = new Semaphore(10);
        read_count_mutex = new Semaphore(1);
    }

    public void start_read() throws InterruptedException {
        // 当有读者时忙等

        read_count_mutex.acquire(); // 申请 readcount 的修改权
        if (getReadercount() == 0)
            rmutex.acquire();// 申请读者数目资源信号量
        readercount++; // 修改读者计数
        read_count_mutex.release(); // 释放 readcount 的修改权



    }

    public void finish_read() throws InterruptedException {
        read_count_mutex.acquire(); // 申请 readcount 的修改权
        readercount--; // 修改读者计数
        if (getReadercount() == 0)
            rmutex.release();// 释放读者数目资源信号量
        read_count_mutex.release();// 释放 readcount 的修改权


    }

    public void start_write() throws InterruptedException {
        // 当有读者时忙等
        /*while (getReadercount() != 0) {
        }*/
        wmutex.acquire(); // 获取写者信号量
    }

    public void finish_write() {
        // 释放写者信号量
        wmutex.release();
    }

    // 读操作
    public String read() throws InterruptedException {
        return data_str;
    }

    // 写操作
    public String write(String new_data) throws InterruptedException {
        this.data_str = new_data;
        return data_str;
    }

    public int getReadercount() {
        return readercount;
    }

    public void setReadercount(int readercount) {
        this.readercount = readercount;
    }

    @Override
    public void run() {

    }
}