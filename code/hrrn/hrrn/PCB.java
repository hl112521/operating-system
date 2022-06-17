package zj.hrrn;

public class PCB {
    private String name; //作业名
    private int length;  // 作业长度,所需内存大小
    private int printer; // 作业执行所需要的打印机数量
    private int tape; // 作业执行所需要的磁带机数量
    private int runtime; //作业估计运行时间
    private int waittime; // 作业在系统中的等待时间
    private int next; // 指向下一个作业控制块的指针

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPrinter() {
        return printer;
    }

    public void setPrinter(int printer) {
        this.printer = printer;
    }

    public int getTape() {
        return tape;
    }

    public void setTape(int tape) {
        this.tape = tape;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getWaittime() {
        return waittime;
    }

    public void setWaittime(int waittime) {
        this.waittime = waittime;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public PCB() {
    }


    public PCB(String name, int length, int printer, int tape, int runtime, int waittime) {
        this.name = name;
        this.length = length;
        this.printer = printer;
        this.tape = tape;
        this.runtime = runtime;
        this.waittime = waittime;
    }

}
