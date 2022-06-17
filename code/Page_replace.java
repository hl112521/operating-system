package zj.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Page_replace {
    // 最佳置换算法
    // opt 最佳页面置换算法
    static void opt(ArrayList<Integer> frame, ArrayList<Integer> page) {
        System.out.println("============最佳页面置换算法(OPT)============");
        // 物理块数和页面长度
        int n_f = frame.size();
        int n_p = page.size();
        // 页面置换次数
        int n_replace = 0;
        // 判断块:初始每个块对应的页面很大
        ArrayList<Integer> judge = new ArrayList<Integer>(n_f);
        for (int i = 0; i < n_f; i++) {
            judge.add(99);
        }
        for (int i = 0; i < n_p; i++) {
            System.out.print(page.get(i) + "===");
            if (i < n_f) {
                // 预装入
                frame.set(i, page.get(i));
                System.out.println(frame);
            } else {
                if (frame.contains(page.get(i))) {
                    // 页面已经存在在物理快中
                    System.out.println("页面已经存在于物理块");
                } else {
                    // 更新往后页面第一次出现的位置
                    for (int j = 0; j < 3; j++) {
                        int index = 99;
                        for (int k = i + 1; k < n_p; k++) {
                            if (frame.get(j) == page.get(k)) {
                                index = k;
                                break;
                            }
                        }
                        // 更新（
                        judge.set(j, index);
                    }
                    // 根据出现最后的（即judge对应最大的）替换
                    int index_max = judge.indexOf(Collections.max(judge));
                    int rep_page = frame.get(index_max);
                    frame.set(index_max, page.get(i));
                    System.out.print(frame);
                    System.out.println("  替换掉了页面：" + rep_page);
                    n_replace = n_replace + 1;
                }
            }
        }
        System.out.println("===================================");
        System.out.println("页面置换：" + n_replace);
        System.out.println("缺页中断： " + (n_replace + n_f));
        System.out.println("===================================");
        for (int i = 0; i < n_f; i++) {
            frame.set(i, -1);
        }
    }

    // fifo 先行先出算法
    // fifo 先进先出置换算法
    static void fifo(ArrayList<Integer> frame, ArrayList<Integer> page) {
        System.out.println("============先进先出置换算法(FIFO)============");
        // 框和页面长度
        int n_f = frame.size();
        int n_p = page.size();
        // 页面置换次数
        int n_lack = 0;
        // 判断块:初始每个块对应的出现次数
        // 因为在预装入之后才会有相应的判断
        // 使用我将判断的状态直接设置成预装入之后 即为 3 2 1
        ArrayList<Integer> judge = new ArrayList<Integer>(n_f);
        for (int i = 0; i < n_f; i++) {
            judge.add(3-i);
        }
        for (int i = 0; i < n_p; i++) {
            System.out.print(page.get(i) + "===");
            if (i < n_f) {
                // 预装入
                frame.set(i, page.get(i));
                System.out.println(frame);
            } else {
                // 每个页面存在次数加1
                for (int j = 0; j < n_f; j++) {
                    judge.set(j, judge.get(j) + 1);
                }
                if (frame.contains(page.get(i))) {
                    // 页面已经存在在物理块中
                    System.out.println("页面已经存在于物理块");
                } else {
                    // 根据存在最久的（即judge对应最大的）替换
                    int index_max = judge.indexOf(Collections.max(judge));
                    int rep_page = frame.get(index_max);
                    frame.set(index_max, page.get(i));
                    // 将新换进的存在状态设置为1
                    judge.set(index_max, 1);
                    System.out.print(frame);
                    System.out.println("  替换掉了页面：" + rep_page);
                    n_lack = n_lack + 1;
                }
            }
        }
        System.out.println("===================================");
        System.out.println("页面置换：" + n_lack);
        System.out.println("缺页中断： " + (n_lack + n_f));
        System.out.println("===================================");
        for (int i = 0; i < n_f; i++) {
            frame.set(i, -1);
        }
    }

    // lru 最近最久未使用算法
    static void lru(ArrayList<Integer> frame, ArrayList<Integer> page) {
        System.out.println("===========最近最久未使用算法(LRU)===========");
        // 框和页面长度
        int n_f = frame.size();
        int n_p = page.size();
        // 页面置换次数
        int n_lack = 0;
        // 和fifo类似先设置为 3 2 1
        ArrayList<Integer> judge = new ArrayList<Integer>(n_f);
        for (int i = 0; i < n_f; i++) {
            judge.add(3 - i);
        }

        for (int i = 0; i < n_p; i++) {
            System.out.print(page.get(i) + "===");
            if (i < n_f) {
                // 预装入
                frame.set(i, page.get(i));
                System.out.println(frame);
            } else {
                // 每个页面存在次数加1
                for (int j = 0; j < n_f; j++) {
                    judge.set(j, judge.get(j) + 1);
                }
                if (frame.contains(page.get(i))) {
                    // 页面已经存在在物理块中
                    System.out.println("页面已经存在于物理块");
                    // 将页面的使用重置为1
                    judge.set(frame.indexOf(page.get(i)), 1);
                } else {
                    // 根据最久未使用的（即judge对应最大的）替换
                    int index_max = judge.indexOf(Collections.max(judge));
                    int rep_page = frame.get(index_max);
                    frame.set(index_max, page.get(i));
                    // 将新换进的使用状态设置为1
                    judge.set(index_max, 1);
                    System.out.print(frame);
                    System.out.println("  替换掉了页面：" + rep_page);
                    n_lack = n_lack + 1;
                }
            }
        }
        System.out.println("===================================");
        System.out.println("页面置换：" + n_lack);
        System.out.println("缺页中断： " + (n_lack + n_f));
        System.out.println("===================================");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入物理块数：");
        int n_frame = scanner.nextInt(); // 物理页框数
        ArrayList<Integer> frame = new ArrayList<Integer>(n_frame);
        for (int i = 0; i < n_frame; i++) {
            frame.add(-1);
        }
        System.out.print("请输入页面走向：");
        scanner.nextLine(); // 控制输入格式
        String inputPages = scanner.nextLine();
        String[] split = inputPages.split(" ");
        int n_page = split.length; // 作业的页面走向总次数
        ArrayList<Integer> page = new ArrayList<Integer>(n_page); // 作业的页面走向
        for (int i = 0; i < n_page; i++) {
            page.add(Integer.parseInt(split[i]));
        }
        scanner.close();
        // 测试输入
        // 3
        // 7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1
        opt(frame, page);
        fifo(frame, page);
        lru(frame, page);
    }
}
