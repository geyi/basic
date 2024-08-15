package com.airing.leecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。
 * 请你返回所有和为 0 且不重复的三元组。
 *
 * @author GEYI
 * @date 2024年08月15日 16:39
 */
public class ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        int len = nums.length;
        Arrays.sort(nums);
        List<List<Integer>> retList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int right = len - 1;
            int target = -nums[i];
            for (int j = i + 1; j < len; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                while (nums[j] < nums[right] && nums[j] + nums[right] > target) {
                    --right;
                }

                if (j == right) {
                    break;
                }

                if (nums[j] + nums[right] == target) {
                    retList.add(Arrays.asList(nums[i], nums[j], nums[right]));
                }
            }
        }
        return retList;
    }
}
