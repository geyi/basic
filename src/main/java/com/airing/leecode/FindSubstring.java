package com.airing.leecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FindSubstring {
    public static void main(String[] args) {
        System.out.println(findSubstring("lingmindraboofooowingdingbarrwingmonkeypoundcake", new String[]{"fooo","barr","wing","ding","wing"}));
    }

    public static List<Integer> findSubstring(String s, String[] words) {
        int len = words.length;
        int wordLen = words[0].length();
        int subStrLen = len * wordLen;
        if (s.length() < subStrLen) {
            return new ArrayList<>(0);
        }

        List<String> wordList = new ArrayList<>(Arrays.asList(words));

        int start = 0;
        int end = 0;
        int limit = 0;
        List<Integer> list = new ArrayList<>();
        while (start <= s.length() - subStrLen && limit < len) {
            int left = start + limit *  wordLen;
            int right = start + limit *  wordLen + wordLen;
            if (wordList.remove(s.substring(left, right))) {
                if (limit == len - 1) {
                    list.add(start);
                    start = start + 1;
                    limit = 0;
                    wordList = new ArrayList<>(Arrays.asList(words));
                } else {
                    ++limit;
                }
            } else {
                start = start + 1;
                limit = 0;
                if (wordList.size() < len) {
                    wordList = new ArrayList<>(Arrays.asList(words));
                }
            }

        }

        return list;
    }
}
