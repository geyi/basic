package com.airing.leecode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomizedSet {
    List<Integer> list = new ArrayList<>();
    Map<String, Integer> map = new HashMap<>();

    public RandomizedSet() {
    }

    public boolean insert(int val) {
        Integer i = map.get(val + "");
        if (i == null) {
            list.add(val);
            map.put(val + "", list.size() - 1);
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(int val) {
        Integer i = map.get(val + "");
        if (i == null) {
            return false;
        } else {
            Integer last = list.get(list.size() - 1);
            list.set(i, last);
            list.remove(list.size() - 1);
            map.put(last + "", i);
            map.remove(val + "");
            return true;
        }
    }

    public int getRandom() {
        return list.get(new Random().nextInt(list.size()));
    }
}
