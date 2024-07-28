package com.airing.leecode;

public class Trap {
    public static void main(String[] args) {
        int[] height = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(trap(height));
    }

    public static int trap(int[] height) {
        int length = height.length;
        int[] left = new int[length];
        int[] right = new int[length];

        int leftMax = 0;
        for (int i = 0; i < length; i++) {
            if (height[i] >= leftMax) {
                leftMax = height[i];
            }
            left[i] = leftMax;
        }

        int rightMax = 0;
        for (int i = length - 1; i >= 0; i--) {
            if (height[i] >= rightMax) {
                rightMax = height[i];
            }
            right[i] = rightMax;
        }

        int ret = 0;
        for (int i = 0; i < length; i++) {
            ret += Math.min(left[i], right[i]) - height[i];
        }

        return ret;
    }
}
