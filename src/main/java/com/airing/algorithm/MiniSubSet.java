package com.airing.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 求连续的最小子集
 */
public class MiniSubSet {
    public static void main(String[] args) {
        int[] arr = {1, 4, 7, 13, 16, 9, 11, 4, 66, 22};
        int num = 19;
        for (String s : test(arr, num)) {
            System.out.println(s);
        }
    }

    private static List<String> test(int[] arr, int num) {
        List<String> ret = new ArrayList<>();
        int len = arr.length;
        int maxSubLen = len - 1;
        for (int i = 1; i <= maxSubLen; i++) { // 最小子级从1开始
            int limit = len - i + 1; // 保证从索引为(limit - 1)的位置开始也能够取到最小子级（获取最小子级时能够遍历到的最后一个元素的位置）
            for (int j = 0; j < limit; j++) {
                int sum = 0;
                StringBuilder sb = new StringBuilder();
                for (int k = j; k < j + i; k++) { // j 表示获取最小子级时的开始位置，j + i 表示获取最小子级时的结束位置（不包含）
                    sum += arr[k];
                    sb.append(",").append(arr[k]);
                }
                if (sum >= num) {
                    ret.add(sb.substring(1));
                }
            }
        }
        return ret;
    }
}
