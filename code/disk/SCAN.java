package zj.disk;

import java.util.ArrayList;
import java.util.Arrays;

public class SCAN extends DiskDispatch{
    /**
     * trackSequence为SCAN调度的磁道号数组
     * len 为传出的磁道号数目
     * pos 为下一个要访问的下标
     * flag 判断是否到了末尾
     */
    private int[] trackSequence;
    private int len;
    int pos;
    boolean flag = false;


    // 构造器
    /**
     * start为磁道开始点
     * track为将要访问的磁道号数组
     * distanceSum移动距离总磁道数
     * movdistan为将要计算的移动距离
     */
    public SCAN(int start, ArrayList<Integer> track) {
        // 给列表排序
        this.start = start;
        this.track = track;;
        this.len = track.size();
        movdistan = new int[len];
        trackSequence = new int[len];
    }

    /**
     * 调度执行函数，进行此次最短寻道时间优先磁盘调度
     */
    public void run() {
        // 初始化磁针位置
        int needle = start;
        for (int i = 0; i < len; i++) {
            // 求出移动距离的磁道号以及移动距离
            Track tc = shortest(needle, track);
            // 将算出的将要访问的下一磁道号、移动距离加入对应数组
            trackSequence[i] = tc.diskName;
            movdistan[i] = tc.distance;
            distanceSum += movdistan[i];
            // 更新指针位置以及磁道号列表，去除已经访问的磁道号
            needle = tc.diskName;
            // 此处使用包装类包装，避免当成索引
            track.remove(Integer.valueOf(tc.diskName));
        }
    }

    /**
     *
     * @param needle 磁针当前位置
     * @param array  访问磁道号数组,即查找范围
     * @return 查找到的要访问的磁道号
     */
    public Track shortest(int needle, ArrayList<Integer> array) {
        // 各变量初始化 先默认第一个是起始磁道
        Track tc = new Track();
        int pos = getPos(needle, track);
        tc.diskName = array.get(pos);
        tc.distance = distance(needle, array.get(pos));
        return tc;
    }
    public int getPos(int start, ArrayList<Integer> array)
    {
        for(int i = 0; i<array.size(); i++) {
            if (start < array.get(i) && !flag){
                pos = i;
                break;
            } else
                pos = array.size()-1;
            if (i == array.size() - 1)
                flag = true;
        }
        return pos;
    }

    @Override
    public String toString() {
        return "\n循环扫描算法SCAN" +
                "\n从" + start + "号磁道开始" +
                "\n被访问的下一个磁道号\t" + Arrays.toString(trackSequence) +
                "\n移动距离（磁道数）\t" + Arrays.toString(movdistan) +
                "\n总道数："+distanceSum+"\t平均寻道长度：" + String.format("%.2f", (double) distanceSum / len);
    }
}
