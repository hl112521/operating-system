package zj.rr;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;


public class RR {
    private int mProcessCount; //进程数
    private Queue<Process> mReadyQueue; // 就绪队列，存放“待运行的进程
    private Queue<Process> mUnreachQueue; // 存放“到达时间未到的进程”
    private Queue<Process> mExecutedQueue; // 执行完毕的进程队列
    private int mTimeSlice; //时间片
    private boolean flag = false;  // 判断在一个时间片内是否能执行完毕
    private double mTotalWholeTime = 0.0;  // 总的周转时间
    private double mTotalWeightWholeTime = 0.0; // 总的带权周转时间

    // 初始化数据
    private RR(int processCount, Queue<Process> processQueue, int timeSlice) {
        this.mProcessCount = processCount;
        this.mUnreachQueue = processQueue;
        mReadyQueue = new LinkedBlockingQueue<>(); // 初始化就绪队列为空
        this.mTimeSlice = timeSlice;
        mExecutedQueue = new LinkedList<>(); // 初始化执行队列为空
    }

    /**
     * RR 算法实现
     */
    public void RRAlgorithm() {
        mReadyQueue.add(mUnreachQueue.poll());
        Process currProcess = mReadyQueue.poll();
        // 第一个进程执行
        int currTime = executeProcess(currProcess, currProcess.getArrivalTime());
        while(mExecutedQueue.size() != mProcessCount) {
            // 把所有“到达时间”达到的进程加入运行队列头部
            while(!mUnreachQueue.isEmpty()) {
                if(mUnreachQueue.peek().getArrivalTime() <= currTime) {
                    mReadyQueue.add(mUnreachQueue.poll());
                } else {
                    break;
                }
            }
            // 一个时间片内不能运行完毕, 就将进程加入就绪队列
//            if(currProcess.getRemainServiceTime() > 0) mReadyQueue.add(currProcess);
            if(!flag) mReadyQueue.add(currProcess);
            //运行队列不为空时
            if(!mReadyQueue.isEmpty()) {
                currProcess = mReadyQueue.poll(); // 从就绪队列中取出一个进程去执行
                currTime = executeProcess(currProcess, currTime);
            } else {
                // 当前没有进程执行，但还有进程为到达，时间直接跳转到到达时间
                currTime = mUnreachQueue.peek().getArrivalTime();
            }
        }
    }

    /**
     * 在一个时间片内,调度进程
     * @param currProcess 需要执行的进程
     * @param currTime 开始执行时间
     * @return 返回运行一个时间片之后的时间
     */
    private int executeProcess(Process currProcess, int currTime) {
        if(currProcess.getRemainServiceTime() - mTimeSlice <= 0) {
            //当前进程在这个时间片内能执行完毕
            flag = true; // 当前进程执行完毕, 标志位设置为true
            showExecuteMessage(currTime, currTime += currProcess.getRemainServiceTime(), currProcess, flag);
            currProcess.setFinishTime(currTime);  // 设置进程运行完成时间
            currProcess.setRemainServiceTime(0); // 设置还需运行时间为0
            calculeteWholeTime(currProcess); // 计算周转时间
            calculateWeightWholeTime(currProcess); // 计算带权周转时间
            mTotalWholeTime += currProcess.getWholeTime(); // 计算周转时间之和
            mTotalWeightWholeTime += currProcess.getWeightWholeTime(); // 计算带权周转之和
            mExecutedQueue.add(currProcess);  // 将进程加入执行完毕队列
        } else {
            //不能执行完毕
            flag = false;
            showExecuteMessage(currTime, currTime += mTimeSlice, currProcess, flag);
            // 设置还需进程执行时间 = 当前还需执行的时间 - 一个时间片的时间
            currProcess.setRemainServiceTime(currProcess.getRemainServiceTime() - mTimeSlice);
        }
        return currTime;
    }

    /**
     * 保留两位小数
     * @param val 需要转换的数据
     * @return 转换完毕的数据
     */
    private Double decimalConversion(double val) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(val));
    }

    /**
     * 计算周转时间
     * @param process 进程
     */
    private void calculeteWholeTime(Process process) {
        // 周转=完成-运行
        process.setWholeTime(process.getFinishTime() - process.getArrivalTime());
    }

    /**
     * 计算带权周转时间
     * @param process 进程
     */
    private void calculateWeightWholeTime(Process process) {
        // 带权=周转/运行
        process.setWeightWholeTime(decimalConversion(process.getWholeTime() / (double)process.getServiceTime()));
    }

    /**
     * 展示运行信息
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param process 进程
     * @param flag 一个时间片内能执行完毕
     */
    private void showExecuteMessage(int startTime, int endTime, Process process, boolean flag) {
        System.out.print(startTime + "～" + endTime + "：\t【进程" + process.getName() + "】运行\t");
        System.out.print("\t就绪队列: [");
        for (Process p: mReadyQueue)
            System.out.print(" " + p.getName() + " ");
        if (!mUnreachQueue.isEmpty())
            System.out.print(" " + mUnreachQueue.peek().getName() + " ");
        if (!flag)
            System.out.print(" " + process.getName() + " ");
        System.out.println("]");

    }

    /**
     * 展示结果
     */
    public void showResult() {
        System.out.print("进程名\t");
        System.out.print("到达时间\t");
        System.out.print("完成时间\t");
        System.out.print("周转时间\t");
        System.out.println("带权周转时间	");
        Process process ;
        while(!mExecutedQueue.isEmpty()) {
            process = mExecutedQueue.poll();
            System.out.print("进程" + process.getName() + "\t");
            System.out.print("\t" + process.getArrivalTime() + "\t");
            System.out.print("\t" + process.getFinishTime() + "\t");
            System.out.print("\t" + process.getWholeTime() + "\t");
            System.out.println("\t" + process.getWeightWholeTime() + "\t");
        }
        System.out.println("平均周转时间：" + decimalConversion(mTotalWholeTime / (double) mProcessCount));
        System.out.println("平均带权周转时间：" + decimalConversion(mTotalWeightWholeTime / (double) mProcessCount));
    }

    /**
     * 打印进程
     * @param queue 输入的进程序列
     */
    public static void printAll(Queue<Process> queue) {
        System.out.print("进程名	");
        System.out.print("到达时间	");
        System.out.println("服务时间	");
        Process process = null;
        while (!queue.isEmpty()){
            process = queue.poll();
            System.out.print("进程" + process.getName() + "	");
            System.out.print("	" + process.getArrivalTime() + "	");
            System.out.println("	" + process.getServiceTime() + "	");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入进程数目: ");
        int processCount = scanner.nextInt();
        if(processCount < 1) return;
        Queue<Process> queue = new LinkedBlockingQueue<>();
        System.out.println("请输入进程号 到达时间 运行时间: ");
        String str = scanner.nextLine();
        for (int i = 0; i < processCount; i++) {
            str = scanner.nextLine();
            String[] s = str.split(" ");
            Process process = new Process(s[0]);
            process.setArrivalTime(Integer.parseInt(s[1]));
            process.setServiceTime(Integer.parseInt(s[2]));
            process.setRemainServiceTime(Integer.parseInt(s[2]));
            queue.add(process);
        }
        System.out.print("输入时间片大小: ");
        int timeSlice = scanner.nextInt();

        RR rr = new RR(processCount, queue, timeSlice);

        System.err.println("=================进程情况=================");
        Thread.sleep(1000);
        printAll(new LinkedBlockingQueue<>(queue));

        System.err.println("=================运行过程=================");
        Thread.sleep(1000);
        rr.RRAlgorithm();
        Thread.sleep(1000);

        System.err.println("=================运行结果=================");
        Thread.sleep(1000);
        rr.showResult();
    }

}
