package zj.disk;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // 磁盘号顺序
        int[] track = new int[]{55, 58, 39, 18, 90, 160, 150, 38, 184};
        // 磁盘列表
        ArrayList<Integer> ta = new ArrayList<>();
        for (int t : track) {
            ta.add(t);
        }

        // 先来先服务
        FCFS ff = new FCFS(100, ta);
        ff.run();
        System.out.println(ff);

        //最短寻道时间优先
        SSTF st = new SSTF(100, ta);
        st.run();
        System.out.println(st);

        // 扫描算法
        Arrays.sort(track);
        for (int t : track) {
            ta.add(t);
        }
        System.out.println(Arrays.toString(track));
        System.out.println();
        SCAN sc = new SCAN(100, ta);
        sc.run();
        System.out.println(sc);


        // 循环扫描算法
        Arrays.sort(track);
        for (int t : track) {
            ta.add(t);
        }
        System.out.println();
        CSCAN csc = new CSCAN(100, ta);
        csc.run();
        System.out.println(csc);

    }
}
