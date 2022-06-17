package zj.reader;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int writerNumber=2; // 写者数量
        int readerNumber=2; // 读者数量
        int diskNumber=1; // 磁盘数量
        Disk[] disks=new Disk[diskNumber];
        Thread[] writer_threads=new Thread[writerNumber];  // 写者线程
        Writer[] writers=new Writer[writerNumber]; // 写者
        Thread[] reader_threads=new Thread[readerNumber];  // 读者线程
        Reader[] readers=new Reader[readerNumber]; //读者
        Thread[] disk_threads=new Thread[diskNumber];  // 磁盘线程


        for (int i=0;i<diskNumber;i++)
        {
            disks[i]=new Disk();
            disk_threads[i]=new Thread(disks[i]); // 模拟磁盘启动线程
            disk_threads[i].start();
        }
        // 先启动一半写者, 确保读者有数据可读
        for (int i=0;i<writerNumber/2;i++)
        {
            writers[i]=new Writer(disks[0]);
            writer_threads[i]=new Thread(writers[i]);
            writer_threads[i].start();
        }
        Thread.sleep(100); // 暂停等待写者启动完毕
        // 启动全部读者线程, 让读者开始读
        for (int i=0;i<readerNumber;i++)
        {
            readers[i]=new Reader(disks[0]);
            reader_threads[i]=new Thread(readers[i]);
            reader_threads[i].start();
        }
        // 将剩余的写者启动
        for (int i=writerNumber/2;i<writerNumber;i++)
        {
            writers[i]=new Writer(disks[0]);
            writer_threads[i]=new Thread(writers[i]);
            writer_threads[i].start();
        }

    }
}
