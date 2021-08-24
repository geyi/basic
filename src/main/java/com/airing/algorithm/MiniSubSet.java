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
        for (int i = 1; i <= maxSubLen; i++) {
            int limit = len - i + 1;
            for (int j = 0; j < limit; j++) {
                int sum = 0;
                StringBuilder sb = new StringBuilder();
                for (int k = j; k < j + i; k++) {
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
