package com.airing.leecode;

import java.util.Arrays;

public class Candy {
    public static void main(String[] args) {
        int[] ratings = {1,2,87,87,87,2,1};
        System.out.println(candy(ratings));
    }

    public static int candy(int[] ratings) {
        int length = ratings.length;
        int[] nums = new int[length];
        Arrays.fill(nums, 1);
        for (int i = 1; i < length; i++) {
            while (ratings[i] > ratings[i - 1] && nums[i] <= nums[i - 1]) {
                nums[i] += 1;
            }
        }

        for (int i = length - 2; i >= 0; i--) {
            while (ratings[i] > ratings[i + 1] && nums[i] <= nums[i + 1]) {
                nums[i] += 1;
            }
        }

        int ret = 0;
        for (int num : nums) {
            ret += num;
        }
        return ret;
    }

}
