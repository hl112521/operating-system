package zj.reader;

public class Reader implements Runnable {
    private Disk disk;

    public Reader(Disk disk) {
        this.disk = disk;
    }

    @Override
    public void run() {
        for (int i=10;i>=0;i--)
        {
            try {
                read(); // 读操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000); // 暂停1s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void read() throws InterruptedException {
        disk.start_read();
        String str=disk.read();  // 读取数据并打印数据
        System.out.println("读操作："+Thread.currentThread().getId()+" 现在的数据为："+str);
        disk.finish_read();
    }

}
