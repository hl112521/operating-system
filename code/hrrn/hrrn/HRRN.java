package zj.hrrn;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

public class HRRN {
    final static int n = 10; // 系统可以调用的作业数目
    static int head, tape, printer;  // 作业头指针, 磁带机数量, 打印机数量
    static long memory;  // 内存大小
    static PCB pcbtable[] = new PCB[n]; //作业表
    static int pcbcount = 0; //系统内当前作业数量

    /**
     * 高响应比算法实现
     */
    static void hrrn() {
        double xk=0, k=0;  // 响应比, xk和k用于比较是否还有满足比当前进程小的响应比
        int p, q, s, t = -1;  // p: 当前运行的作业id, q:是否执行完毕的作业id, s: 下一个需要执行的id, t:是否为第一个作业
        do {
            p = head;
            q = s = -1;
            k = 0;
            while (p != -1) {
                // 系统可用资源数量是否满足作业需求
                if (pcbtable[p].getLength() <= memory && pcbtable[p].getTape() <= tape && pcbtable[p].getPrinter() <= printer) {
                    // 计算优先级, 优先级=等待时间+服务时间/服务时间
                    xk = (double) (pcbtable[p].getWaittime() + pcbtable[p].getRuntime()) / pcbtable[p].getRuntime();
                    // 让优先级保持两位小数
                    DecimalFormat df = new DecimalFormat("0.00");
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    xk = Double.parseDouble(df.format(xk));
                    // 满足条件的第一个作业 或者作业q的响应比小于作业p的响应比[找到响应比最大的作业]
                    if (q == 0 || xk > k) {
                        k = xk;
                        q = p;
                        t = s;
                    }
                }
                s = p;
                p = pcbtable[p].getNext(); // 指向下一个作业
            }
            if (q != -1) {  // 判断是否调度完毕
                if (t == -1)  // 是作业队列的第一个
                    head = pcbtable[head].getNext(); // 指向下一个作业
                else
                    pcbtable[t].setNext(pcbtable[q].getNext());  // 设置当前作业的下一个作业
                memory = memory - pcbtable[q].getLength(); // 为作业分配内存空间
                tape = tape - pcbtable[q].getTape(); // 为作业分配磁带机
                printer = printer - pcbtable[q].getPrinter(); // 为作业分配打印机
                System.out.println("选中的作业名称: " + pcbtable[q].getName() + ", 优先级: " + k);
                // 调度完成归还
                memory += pcbtable[q].getLength();
                tape += pcbtable[q].getTape();
                printer += pcbtable[q].getPrinter();
            }
        } while (q != -1);
    }

    /**
     * 将输入的字符串,变成进程信息
     * @param str 输入的字符串
     * @return 转换好的进程信息
     */
    static PCB  input(String str) {
        String[] s = str.split(" ");
        // 作业名称, 作业大小, 打印机数, 磁带机数, 运行时间, 等待时间
        return new PCB(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[3]), Integer.parseInt(s[2]), Integer.parseInt(s[5]), Integer.parseInt(s[4]));
    }

    public static void main(String[] args) {
        int p;
        memory = 65536; // 内存大小
        tape = 4; // 系统磁带机的数量
        printer = 2; // 系统打印机的数量
        head = -1; // 第一个作业
        System.out.println("请输入相关数据(以作业大小为负数停止输入): "); // 输入数据, 建立队列
        System.out.println("输入作业名, 作业大小, 磁带机数, 打印机数, 等待时间, 估计运行时间");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        // 输入作业相关信息
        PCB pcb = input(str);
        while (pcb.getLength()!= -1) {
            if (pcbcount < n)
                p = pcbcount;
            else {
                System.out.println("无法创建作业!");
                break;
            }
            pcbtable[p] = pcb; // 将作业加入作业列表
            pcbtable[p].setNext(head);  // 指向下一个作业, 最后一个指向的作业为:-1,代表为空
            // C-->B-->A-->-1
            pcbcount++;
            head = p; // 让head指向作业
            str = scanner.nextLine();
            pcb = input(str);
        }
        hrrn();
    }
}
