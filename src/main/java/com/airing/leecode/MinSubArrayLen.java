package com.airing.leecode;

/**
 * 给定一个含有 n 个正整数的数组和一个正整数 target 。
 *
 * 找出该数组中满足其总和大于等于 target 的长度最小的子数组
 * [numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0 。
 *
 * @author GEYI
 * @date 2024年08月15日 19:57
 */
public class MinSubArrayLen {
    public static void main(String[] args) {
        System.out.println(minSubArrayLen(7, new int[]{2,3,1,2,4,3}));
    }

    public static int minSubArrayLen(int target, int[] nums) {
        int len = nums.length;
        int left;
        int right;
        for (int i = 0; i < len; i++) {
            left = 0;
            right = left + i;
            while (right < len) {
                int sum = 0;
                for (int j = left; j <= right; j++) {
                    sum += nums[j];
                }
                if (sum >= target) {
                    return right - left + 1;
                }
                left++;
                right++;
            }
        }
        return 0;
    }

    public static int minSubArrayLen2(int target, int[] nums) {
        int len = nums.length;
        int left = 0;
        int right = 0;
        int min = Integer.MAX_VALUE;
        int sum = 0;
        while (right < len) {
            sum += nums[right];
            while (sum >= target) {
                min = Math.min(min, right - left + 1);
                sum -= nums[left];
                left++;
            }
            right++;
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }
}
