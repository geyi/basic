package com.airing.leecode;

public class Jump {
    public static void main(String[] args) {
        int[] nums = {2, 3, 1, 1, 4};
        System.out.println(jump2(nums));
    }

    /**
     * 给你一个非负整数数组 nums ，你最初位于数组的 第一个下标 。数组中的每个元素代表你在该位置可以跳跃的最大长度。
     * <p>
     * 判断你是否能够到达最后一个下标，如果可以，返回 true ；否则，返回 false 。
     * <p>
     * 思路：对于每一个可以到达的位置 x，它使得 x+1,x+2,⋯,x+nums[x] 这些连续的位置都可以到达
     *
     * @param nums
     * @return boolean
     * @author GEYI
     * @date 2024年07月21日 12:01
     */
    public static boolean jump(int[] nums) {
        int right = 0;
        for (int i = 0; i < nums.length; i++) {
            // 对于每一个可以到达的位置 x，它使得 x+1,x+2,⋯,x+nums[x] 这些连续的位置都可以到达
            if (i <= right) {
                right = Math.max(right, i + nums[i]);
                if (right >= nums.length - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 给定一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]。
     * <p>
     * 每个元素 nums[i] 表示从索引 i 向前跳转的最大长度。换句话说，如果你在 nums[i] 处，你可以跳转到任意 nums[i + j] 处:
     * <ul>
     * <li>0 <= j <= nums[i]</li>
     * <li>i + j < n</li>
     * </ul>
     * 返回到达 nums[n - 1] 的最小跳跃次数。生成的测试用例可以到达 nums[n - 1]。
     * <p>
     * 思路：每次找到可到达的最远位置
     *
     * @param nums
     * @return int
     * @author GEYI
     * @date 2024年07月21日 12:03
     */
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
