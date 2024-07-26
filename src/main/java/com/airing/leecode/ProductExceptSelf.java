package com.airing.leecode;

public class ProductExceptSelf {

    /**
     * 给你一个整数数组 nums，返回 数组 answer ，其中 answer[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积 。
     * <p>
     * 题目数据 保证 数组 nums之中任意元素的全部前缀元素和后缀的乘积都在  32 位 整数范围内。
     * <p>
     * 请 不要使用除法，且在 O(n) 时间复杂度内完成此题。
     *
     * @param nums
     * @return int[]
     * @author GEYI
     * @date 2024年07月26日 16:26
     */
    public static int[] productExceptSelf(int[] nums) {
        int length = nums.length;
        int[] ret = new int[length];
        int idx = 0;
        while (idx < length) {
            int total = 1;
            for (int i = 0; i < length; i++) {
                if (i == idx) {
                    continue;
                }
                total *= nums[i];
            }
            ret[idx++] = total;
        }
        return ret;
    }

    public static int[] productExceptSelf2(int[] nums) {
        int length = nums.length;
        int[] left = new int[length];
        int[] right = new int[length];
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                left[0] = 1;
            } else {
                left[i] = left[i - 1] * nums[i - 1];
            }
        }
        for (int i = length - 1; i >= 0; i--) {
            if (i == length - 1) {
                right[length - 1] = 1;
            } else {
                right[i] = right[i + 1] * nums[i + 1];
            }
        }

        int[] ret = new int[length];
        for (int i = 0; i < length; i++) {
            ret[i] = left[i] * right[i];
        }

        return ret;
    }
}
