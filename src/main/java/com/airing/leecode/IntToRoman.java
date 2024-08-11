package com.airing.leecode;

import java.util.HashMap;
import java.util.Map;

public class IntToRoman {

    public static String intToRoman(int num) {
        Map<Character, Character> map = new HashMap<>((int) (7 / .75) + 1);
//        I	1
//        V	5
//        X	10
//        L	50
//        C	100
//        D	500
//        M	1000

        map.put('1', 'I');
        map.put('5', 'V');
        map.put('1', 'X');
        map.put('D', 'L');


        String s = String.valueOf(num);
        char[] ca = s.toCharArray();
        int len = ca.length;
        int a, b, c, d;
        StringBuffer sb = new StringBuffer();
        int j = 0;
        for (int i = len - 1; i >= 0; i++) {
            if (j == 0) {
                if (ca[i] == '4') {
                    sb.insert(0, "IV");
                } else if (ca[i] == '9') {
                    sb.insert(0, "IX");
                } else {

                }
            }
        }


        return "";
    }
}
