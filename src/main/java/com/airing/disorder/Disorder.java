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
            /**
             * a, b, x, y的初始值都是0
             * a = 1; x = b; 是两条没有关联的语句，所以可以对他们进行指令重排
             * b = 1; y = a; 同上
             * 如果存在x == 0 && y == 0，则说明y = a; 和 x = b;一定在a = 1; 和 b = 1;之前执行了
             * 即发生了指令重排
             */
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
