package zj.reader;

public class Writer implements Runnable{
    private Disk disk;

    public Writer(Disk disk) {
        this.disk = disk;
    }

    @Override
    public void run() {

        for (int i=10;i>=0;i--)
        {
            try {
                write(); // 写操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000); // 等待1s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void write() throws InterruptedException {
        disk.start_write();
        String str=disk.write("data+"+Thread.currentThread().getId()); // 写数据
        System.out.println("写操作："+Thread.currentThread().getId()+" 现在的数据为："+str);
        disk.finish_write();

    }
}
