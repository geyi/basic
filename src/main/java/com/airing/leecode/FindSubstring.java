package com.airing.leecode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FindSubstring {
    public static void main(String[] args) {
        System.out.println(findSubstring("barfoothefoobarman", new String[]{"foo","bar"}));
    }

    public static List<Integer> findSubstring(String s, String[] words) {
        int len = words.length;
        int wordLen = words[0].length();
        int subStrLen = len * wordLen;
        if (s.length() < subStrLen) {
            return new ArrayList<>(0);
        }

        Set<String> set = new HashSet<>();
        for (String word : words) {
            set.add(word);
        }

        int start = 0;
        int end = 0;
        int limit = 0;
        List<Integer> list = new ArrayList<>();
        while (start <= s.length() - subStrLen && limit < wordLen) {
            int left = start + limit *  wordLen;
            int right = start + limit *  wordLen + wordLen;
            if (set.contains(s.substring(left, right))) {
                if (limit == len - 1) {
                    list.add(start);
                    start = right;
                    limit = 0;
                } else {
                    ++limit;
                }
            } else {
                ++start;
            }

        }

        return list;
    }
}
