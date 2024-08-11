package com.airing.leecode;

public class ReverseWords {
    public static void main(String[] args) {
        System.out.println(reverseWords("the sky is blue"));
    }

    public static String reverseWords(String s) {
        String[] arr = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = arr.length - 1; i >= 0; i--) {
            String word = arr[i].trim();
            if (word.length() == 0) {
                continue;
            }
            if (i == 0) {
                sb.append(word);
            } else {
                sb.append(word + " ");
            }
        }
        return sb.toString().trim();
    }
}
