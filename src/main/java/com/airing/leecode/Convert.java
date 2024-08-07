package com.airing.leecode;

import java.util.ArrayList;
import java.util.List;

public class Convert {
    public static void main(String[] args) {
        System.out.println(convert("ABC", 2));
    }

    public static String convert(String s, int numRows) {
        if (numRows == 1 || s.length() == 1 || s.length() <= numRows) {
            return s;
        }
        int len = s.length();
        int gap = numRows - 2;
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        l1.add(0);
        for (int i = 1; i < len; i++) {
            if (gap > 0) {
                l2.add(i);
                gap--;
            } else {
                l1.add(i);
                gap = numRows - 2;
            }
        }

//        print(l1);
//        System.out.println();
//        print(l2);
//        System.out.println();

        char[][] arr = new char[numRows][l1.get(l1.size() - 1)];

        int idx = 0;
        int sIdx = 0;
        for (Integer i : l1) {
            gap = numRows - 2;
            for (int j = 0; j < numRows; j++) {
//                System.out.println(j + "," + i);
                if (sIdx >= len) {
                    break;
                }
                arr[j][i] = s.charAt(sIdx++);
            }
            for (int j = idx; j < l2.size(); j++) {
                int integer = l2.get(j);
                if (gap > 0) {
//                    System.out.println(gap + "," + integer);
                    if (sIdx >= len) {
                        break;
                    }
                    arr[gap][integer] = s.charAt(sIdx++);
                    gap--;
                } else {
                    idx = j;
                    break;
                }
            }
        }

        // 遍历arr二维数组
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < l1.get(l1.size() - 1); j++) {
                if (arr[i][j] != 0) {
                    ret.append(arr[i][j]);
                }
            }
        }

        return ret.toString();
    }

    private static void print(List<Integer> list) {
        for (Integer i : list) {
            System.out.print(i + " ");
        }
    }
}
