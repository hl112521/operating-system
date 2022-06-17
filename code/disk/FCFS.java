package zj.disk;

import java.util.ArrayList;
import java.util.Arrays;

public class FCFS extends DiskDispatch{
    // 构造器
    /**
     * start为磁道开始点
     * track为将要访问的磁道号数组
     * distanceSum移动距离总磁道数
     * movdistan为将要计算的移动距离
     */
    public FCFS(int start, ArrayList<Integer> track) {
        this.start = start;
        this.track = track;
        movdistan = new int[track.size()];
    }

    /**
     * 调度执行函数，进行此次先来先服务磁盘调度
     */
    public void run() {
        // 初始化磁针位置
        int needle = start;
        for (int i = 0; i < track.size(); i++) {
            // 求出移动距离并保存
            movdistan[i] = distance(needle, track.get(i));
            distanceSum += movdistan[i];
            // 更新指针位置
            needle = track.get(i);
        }
    }

    @Override
    public String toString() {
        return "\n先来先服务FCFS" +
                "\n从" + start + "号磁道开始" +
                "\n被访问的下一个磁道号\t" + track +
                "\n移动距离（磁道数）\t" + Arrays.toString(movdistan) +
                "\n总道数："+distanceSum+"\t平均寻道长度：" + String.format("%.2f", (double) distanceSum / track.size());
    }
}
