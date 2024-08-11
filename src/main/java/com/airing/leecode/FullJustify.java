package com.airing.leecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给定一个单词数组 words 和一个长度 maxWidth ，重新排版单词，使其成为每行恰好有 maxWidth 个字符，且左右两端对齐的文本。
 *
 * 你应该使用 “贪心算法” 来放置给定的单词；也就是说，尽可能多地往每行中放置单词。必要时可用空格 ' ' 填充，使得每行恰好有 maxWidth 个字符。
 *
 * 要求尽可能均匀分配单词间的空格数量。如果某一行单词间的空格不能均匀分配，则左侧放置的空格数要多于右侧的空格数。
 *
 * 文本的最后一行应为左对齐，且单词之间不插入额外的空格。
 *
 * @author GEYI
 * @date 2024年08月11日 20:33
 */
public class FullJustify {

    public static void main(String[] args) {
        List<String> strings = fullJustify(new String[]{"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer.", "Art", "is", "everything", "else", "we", "do"}, 20);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    public static List<String> fullJustify(String[] words, int maxWidth) {
        List<String> ret = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word;
            if (line.length() == 0) {
                word = words[i];
            } else {
                word = " " + words[i];
            }
            if (line.length() + word.length() <= maxWidth) {
                line.append(word);
            } else {
                ret.add(line.toString());
                line.setLength(0);
                i--;
            }
        }
        if (line.length() > 0) {
            ret.add(line.toString());
        }

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < ret.size(); j++) {
            String str = ret.get(j);
            if (str.length() < maxWidth) {
                // 需要插入的空格数
                int limit = maxWidth - str.length();
                boolean space = str.endsWith(" ");
                String[] arr = str.split(" ");
                if (arr.length == 1 || j == ret.size() - 1) {
                    sb.append(str);
                    for (int i = 0; i < limit; i++) {
                        sb.append(" ");
                    }
                    ret.set(j, sb.toString());
                    sb.setLength(0);
                    continue;
                }

                int gap = space ? arr.length : arr.length - 1;
                String[] spaceArr = new String[gap];
                Arrays.fill(spaceArr, " ");
                for (int i = 0; i < limit; i++) {
                    int idx = i % gap;
                    spaceArr[idx] = spaceArr[idx] + " ";
                }
                for (int i = 0; i < arr.length; i++) {
                    sb.append(arr[i]);
                    if (i < gap) {
                        sb.append(spaceArr[i]);
                    }
                }
                ret.set(j, sb.toString());
                sb.setLength(0);
            }
        }

        return ret;
    }
}
