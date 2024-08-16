package com.airing.leecode;

/**
 * 给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
 *
 * @author GEYI
 * @date 2024年08月16日 17:41
 */
public class LengthOfLongestSubstring {
    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring(" "));
    }

    public static int lengthOfLongestSubstring(String s) {
        int start = 0;
        int end = 0;
        StringBuilder ret = new StringBuilder();
        int retLen = 0;
        while (end < s.length()) {
            int i = ret.indexOf(s.charAt(end) + "");
            if (i > -1) {
                retLen = Math.max(retLen, ret.toString().length());
                start = i + 1;
                ret.delete(0, start);
            } else {
                ret.append(s.charAt(end));
                ++end;
            }
        }
        return Math.max(retLen, ret.length());
    }
}
