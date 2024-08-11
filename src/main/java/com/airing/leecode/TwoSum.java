package com.airing.leecode;

/**
 * 给你一个下标从 1 开始的整数数组 numbers ，该数组已按 非递减顺序排列  ，请你从数组中找出满足相加之和等于目标数 target 的两个数。如果设这两个数分别是 numbers[index1] 和 numbers[index2] ，
 * 则 1 <= index1 < index2 <= numbers.length 。
 *
 * 以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整数的下标 index1 和 index2。
 *
 * 你可以假设每个输入 只对应唯一的答案 ，而且你 不可以 重复使用相同的元素。
 *
 * 你所设计的解决方案必须只使用常量级的额外空间。
 *
 * @author GEYI
 * @date 2024年08月11日 20:39
 */
public class TwoSum {

    public int[] twoSum(int[] numbers, int target) {
        int len = numbers.length;
        int[] ret = new int[2];
        out: for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                if (numbers[i] + numbers[j] == target) {
                    ret[0] = i + 1;
                    ret[1] = j + 1;
                    break out;
                }
            }
        }
        return ret;
    }

    /**
     * 利用数组有序这一点
     *
     * @param numbers
     * @param target
     * @return int[]
     * @author GEYI
     * @date 2024年08月11日 20:48
     */
    public int[] twoSum2(int[] numbers, int target) {
        int len = numbers.length;
        int[] ret = new int[2];
        int i = 0;
        int j = len - 1;
        while (i < j) {
            int t = numbers[i] + numbers[j];
            if (t == target) {
                ret[0] = i + 1;
                ret[1] = j + 1;
                break;
            } else if (t > target) {
                j--;
            } else {
                i++;
            }
        }
        return ret;
    }
}
