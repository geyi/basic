package com.airing.leecode;

import java.util.HashMap;
import java.util.Map;

public class RomanToInt {
    public static void main(String[] args) {
        System.out.println(romanToInt("III"));
    }

    public static int romanToInt(String s) {
        Map<Character, Integer> map = new HashMap<>((int) (7 / .75) + 1);
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);

        int total = 0;
        char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            boolean flag = false;
            if (c == 'I' && i + 1 < charArray.length) {
                if (charArray[i + 1] == 'V') {
                    total += 4;
                    i++;
                } else if (charArray[i + 1] == 'X') {
                    total += 9;
                    i++;
                } else {
                    flag = true;
                }
            } else if (c == 'X' && i + 1 < charArray.length) {
                if (charArray[i + 1] == 'L') {
                    total += 40;
                    i++;
                } else if (charArray[i + 1] == 'C') {
                    total += 90;
                    i++;
                } else {
                    flag = true;
                }
            } else if (c == 'C' && i + 1 < charArray.length) {
                if (charArray[i + 1] == 'D') {
                    total += 400;
                    i++;
                } else if (charArray[i + 1] == 'M') {
                    total += 900;
                    i++;
                } else {
                    flag = true;
                }
            } else {
                total += map.get(c);
            }
            if (flag) {
                total += map.get(c);
            }
        }

        return total;
    }
}
