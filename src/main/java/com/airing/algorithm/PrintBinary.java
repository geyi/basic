package com.airing.algorithm;

public class PrintBinary {
    public static void main(String[] args) {
        int i = Integer.MAX_VALUE;
        print(i);
        new Sort();
    }

    public static void print(int i) {
        for (int j = 31; j >= 0; j--) {
            System.out.print((i & (1 << j)) == 0 ? "0" : "1");
        }
    }
}
