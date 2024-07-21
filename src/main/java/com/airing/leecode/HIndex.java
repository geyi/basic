package com.airing.leecode;

public class HIndex {
    public static void main(String[] args) {
        int[] arr = {0, 1};
        hIndex(arr);
    }

    /**
     * 给你一个整数数组 citations ，其中 citations[i] 表示研究者的第 i 篇论文被引用的次数。计算并返回该研究者的 h 指数。
     * <p>
     * 根据维基百科上 h 指数的定义：h 代表“高引用次数” ，一名科研人员的 h 指数 是指他（她）至少发表了 h 篇论文，并且 至少 有 h 篇论文被引用次数大于等于 h 。如果 h 有多种可能的值，h 指数 是其中最大的那个。
     *
     * @param citations
     * @return int
     * @author GEYI
     * @date 2024年07月21日 22:27
     */
    public static int hIndex(int[] citations) {
        int length = citations.length;
        for (int i = 0; i < length - 1; i++) {
            int max = citations[i];
            int idx = i;
            for (int j = i + 1; j < length; j++) {
                if (citations[j] >= max) {
                    max = citations[j];
                    idx = j;
                }
            }
            int tmp = citations[idx];
            citations[idx] = citations[i];
            citations[i] = tmp;
        }

        int ret = 0;
        for (int i = 0; i < length; i++) {
            if (i + 1 <= citations[i]) {
                ret++;
            }
        }

        return ret;
    }
}
