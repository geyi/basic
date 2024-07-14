package com.airing.leecode;

import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicates {

    private static int removeDuplicates(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int limit = nums.length;
        int delCount = 0;
        for (int i = 0; i < limit; i++) {
            int num = nums[i];
            if (!set.add(num)) {
                System.arraycopy(nums, i + 1, nums, i, nums.length - i - 1);
                delCount++;
                i--;
                limit--;
            }
        }

        return nums.length - delCount;
    }

    /**
     * 给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使得出现次数超过两次的元素只出现两次 ，返回删除后数组的新长度。
     *
     * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
     *
     * @param nums
     * @return int
     * @author GEYI
     * @date 2024年07月14日 21:14
     */
    private static int removeDuplicates2(int[] nums) {
        int limit = nums.length;
        int delCount = 0;
        int count = 1;
        int curEle = nums[0];
        for (int i = 1; i < limit; i++) {
            int num = nums[i];
            if (curEle == num) {
                if (++count > 2) {
                    System.arraycopy(nums, i + 1, nums, i, nums.length - i - 1);
                    delCount++;
                    i--;
                    limit--;
                }
            } else {
                curEle = num;
                count = 1;
            }
        }
        return nums.length - delCount;
    }
}
