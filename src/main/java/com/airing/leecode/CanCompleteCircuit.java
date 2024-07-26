package com.airing.leecode;

public class CanCompleteCircuit {
    public static void main(String[] args) {
        int[] gas = {3,3,4};
        int[] cost = {3,4,4};
        System.out.println(canCompleteCircuit3(gas, cost));
    }

    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int length = gas.length;
        int limit = length - 1;
        for (int i = 0; i < length; i++) {
            if (gas[i] < cost[i]) {
                continue;
            }

            int gasTotal = gas[i];
            int costTotal = cost[i];
            int j = i == limit ? 0 : i + 1;
            while (j != i) {
                gasTotal += gas[j];
                costTotal += cost[j];
                if (gasTotal < costTotal) {
                    break;
                }
                j = j == limit ? 0 : j + 1;
            }
            if (gasTotal >= costTotal) {
                return i;
            }
        }
        return -1;
    }

    public static int canCompleteCircuit3(int[] gas, int[] cost) {
        int length = gas.length;
        int gt = 0;
        int ct = 0;
        out: for (int i = 0; i < length; i++) {
            gt += gas[i];
            ct += cost[i];
            if (gas[i] < cost[i]) {
                continue;
            }

            int gasTotal = gas[i];
            int costTotal = cost[i];
            int j = i + 1;
            while (j < length) {
                gasTotal += gas[j];
                costTotal += cost[j];
                if (gasTotal < costTotal) {
                    break;
                }
                j++;
            }
            gasTotal += gt - gas[i];
            costTotal += ct - cost[i];
            if (gasTotal >= costTotal) {
                return i;
            } else {
                gt = gasTotal;
                ct = costTotal;
                i = j;
            }
        }
        return -1;
    }

    public static int canCompleteCircuit2(int[] gas, int[] cost) {
        int n = gas.length;
        int i = 0;
        while (i < n) {
            int sumOfGas = 0, sumOfCost = 0;
            int cnt = 0;
            while (cnt < n) {
                int j = (i + cnt) % n;
                sumOfGas += gas[j];
                sumOfCost += cost[j];
                if (sumOfCost > sumOfGas) {
                    break;
                }
                cnt++;
            }
            if (cnt == n) {
                return i;
            } else {
                i = i + cnt + 1;
            }
        }
        return -1;
    }
}
