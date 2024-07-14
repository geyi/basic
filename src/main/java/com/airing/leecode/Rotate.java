package com.airing.leecode;

public class Rotate {

    public static void rotate(int[] nums, int k) {
        int i = k % nums.length;
        if (i == 0) {
            return;
        }

        int[] newNums = new int[nums.length];
        for (int j = 0; j < nums.length; j++) {
            newNums[(j + k) % nums.length] = nums[j];
        }
        System.arraycopy(newNums, 0, nums, 0, nums.length);
    }

}
