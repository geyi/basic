package com.airing.leecode;

public class Jump {
    public static void main(String[] args) {
        int[] nums = {2,3,1,1,4};
        System.out.println(jump2(nums));
    }

    public static boolean jump(int[] nums) {
        int right = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i <= right) {
                right = Math.max(right, i + nums[i]);
                if (right >= nums.length - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int jump2(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }
        int right;
        int ret = 0;
        for (int i = 0; i < nums.length; i++) {
            right = (i + nums[i]);
            if (right >= nums.length - 1) {
                ret++;
                break;
            }
            int max = 0;
            int next = 0;
            for (int j = i + 1; j <= right; j++) {
                if ((j + nums[j]) > max) {
                    max = j + nums[j];
                    next = j;
                }
            }
            if (next >= nums.length - 1) {
                ret++;
                break;
            } else {
                i = next - 1;
                ret++;
            }
        }
        return ret;
    }
}
