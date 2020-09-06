package com.airing.disorder;

/**
 * 指令重排证明
 */
public class Disorder {
    private static int a, b;
    private static int x, y;

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        int i = 0;
        while (true) {
            i++;
            a = 0;
            b = 0;
            x = 0;
            y = 0;
            Thread one = new Thread(() -> {
               a = 1;
               x = b;
            });
            Thread other = new Thread(() -> {
               b = 1;
               y = a;
            });
            one.start();
            other.start();
            one.join();
            other.join();
            if (x == 0 && y == 0) {
                String result = "第" + i + "次（x = " + x + ", y = " + y + ")";
                System.out.println(result);
                System.out.println(System.currentTimeMillis() - start);
                break;
            } else {

            }
        }
    }
}
