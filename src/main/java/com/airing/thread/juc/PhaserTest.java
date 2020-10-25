package com.airing.thread.juc;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserTest {

    static class MarriagePhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println(registeredParties + "人已到达，开动！");
                    System.out.println("-------------");
                    return false;
                case 1:
                    System.out.println(registeredParties + "人已吃完，解散！");
                    System.out.println("-------------");
                    return false;
                case 2:
                    System.out.println(registeredParties + "人已离开！");
                    System.out.println("-------------");
                    return false;
                case 3:
                    if (registeredParties == 2) {
                        System.out.println("🤵👰抱抱");
                    }
                    return false;
                default:
                    return true;
            }
        }
    }

    private static Random random = new Random();
    private static MarriagePhaser phaser = new MarriagePhaser();

    private static void milliSleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Person implements Runnable {

        private String name;

        public Person(String name) {
            this.name = name;
        }

        private void arrive() {
            milliSleep();
            System.out.println(name + "到达");
            phaser.arriveAndAwaitAdvance();
        }

        private void eat() {
            milliSleep();
            System.out.println(name + "吃完了");
            phaser.arriveAndAwaitAdvance();
        }

        private void leave() {
            milliSleep();
            System.out.println(name + "离开了");
            phaser.arriveAndAwaitAdvance();
        }

        private void hug() {
            if ("新郎".equals(name) || "新娘".equals(name)) {
                milliSleep();
                System.out.println(name + "洞房");
                phaser.arriveAndAwaitAdvance();
            } else {
                phaser.arriveAndDeregister();
            }
        }

        @Override
        public void run() {
            arrive();
            eat();
            leave();
            hug();
        }
    }

    public static void main(String[] args) {
        phaser.bulkRegister(7);

        for (int i = 0; i < 5; i++) {
            new Thread(new Person("person" + i)).start();
        }

        new Thread(new Person("新郎")).start();
        new Thread(new Person("新娘")).start();
    }

}
