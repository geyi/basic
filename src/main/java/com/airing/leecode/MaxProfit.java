package com.airing.leecode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
 * <p>
 * 在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。你也可以先购买，然后在 同一天 出售。
 * <p>
 * 返回 你能获得的 最大 利润 。
 *
 * @author GEYI
 * @date 2024年07月18日 15:29
 */
public class MaxProfit {
    public static void main(String[] args) {
        int[] prices = {3, 2, 6, 5, 0, 3};
        System.out.println(maxProfit(prices));

        String regex = "(?<!拒)[，。！：；,.!:;？?（]?(收)[），。！：；,.!:;？?]?";
        Pattern pattern = Pattern.compile(regex);

        // 示例文本
        String[] testStrings = {
                "，收。",
                "收",
                "（收）",
                "。收，",
                "拒收",
                "拒收。",
                "，拒收。"
        };

        for (String testString : testStrings) {
            Matcher matcher = pattern.matcher(testString);
            if (matcher.find()) {
                System.out.println("Matched: " + matcher.group(0));
            } else {
                System.out.println("No match: " + testString);
            }
        }
    }

    public static int maxProfit(int[] prices) {
        /*int profit = 0;
        int tmp = Integer.MIN_VALUE;
        for (int price : prices) {
            int newProfit = Math.max(profit, tmp + price);
            tmp = Math.max(tmp, profit - price);
            profit = newProfit;
        }
        return profit;*/

        int profit = 0;
        int startIdx = 0;
        int endIdx = 0;
        boolean last = false;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] - prices[i - 1] > 0) {
                if (!last) {
                    startIdx = i - 1;
                }
                endIdx = i;
                last = true;
                if (i == prices.length - 1) {
                    profit += prices[endIdx] - prices[startIdx];
                }
            } else {
                if (endIdx > startIdx) {
                    profit += prices[endIdx] - prices[startIdx];
                    startIdx = 0;
                    endIdx = 0;
                }
                last = false;
            }
        }

        return profit;
    }

}
