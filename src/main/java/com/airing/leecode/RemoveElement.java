package com.airing.leecode;

public class RemoveElement {

    public static void main(String[] args) {
        int[] nums = {0, 1, 2, 2, 3, 0, 4, 2};
        int val = 2;
        System.out.println(remove2(nums, val));
        for (int num : nums) {
            System.out.print(num + " ");
        }
    }

    public static int remove(int[] nums, int val) {
        if (nums.length == 0) {
            return 0;
        }
        int[] newNums = new int[nums.length];
        int i = 0;
        for (int num : nums) {
            if (num != val) {
                newNums[i++] = num;
            }
        }
        int j = 0;
        for (int newNum : newNums) {
            nums[j++] = newNum;
        }
        return i;
    }

    public static int remove2(int[] nums, int val) {
        if (nums.length == 0) {
            return 0;
        }
        int idx = 0;
        int limit = nums.length;
        for (int i = idx; i < limit; i++) {
            if (nums[i] == val) {
                System.arraycopy(nums, i + 1, nums, i, nums.length - i - 1);
                i--;
                limit--;
            }
        }
        return limit;
    }
}
