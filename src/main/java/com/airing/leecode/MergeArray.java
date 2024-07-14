package com.airing.leecode;

import java.util.Arrays;

public class MergeArray {
    public static void main(String[] args) {
        int[] nums1 = {-1,0,0,3,3,3,0,0,0};
        int m = 6;
        int[] nums2 = {1, 2, 2};
        int n = 3;
        merge(nums1, m, nums2, n);
        for (int i : nums1) {
            System.out.print(i + " ");
        }
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        if (m == 0) {
            for (int i : nums2) {
                nums1[m++] = i;
            }
            return;
        }
        int idx = 0;
        int limit = m;
        for (int i = 0; i < n; i++) {
            for (int j = idx; j < limit; j++) {
                if (nums2[i] > nums1[j]) {
                    if (j == limit - 1) {
                        nums1[limit] = nums2[i];
                        idx = limit;
                    } else if (nums2[i] <= nums1[j + 1]) {
                        System.arraycopy(nums1, j + 1, nums1, j + 2, limit - j - 1);
                        nums1[j + 1] = nums2[i];
                        idx = j + 1;
                    } else {
                        continue;
                    }
                    limit++;
                    break;
                } else {
                    System.arraycopy(nums1, j, nums1, j + 1, limit - j);
                    nums1[j] = nums2[i];
                    limit++;
                    break;
                }
            }
        }
    }
}
