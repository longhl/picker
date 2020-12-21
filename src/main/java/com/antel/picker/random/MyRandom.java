package com.antel.picker.random;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyRandom {

    private static Random random = new Random();

    public static <T> List<T> pick(List<T> basePool, int resultNum) {

        if (resultNum < 0) {
            throw new IllegalArgumentException("resultNum must >= 0");
        }

        if (CollectionUtils.isEmpty(basePool)) {
            throw new IllegalArgumentException("base list is empty");
        }
        List<T> copyList = new ArrayList<>(basePool);
        List result = new ArrayList<T>();
        if (resultNum >= copyList.size()) {
            result.addAll(copyList);
        } else {
            for (int i = 0; i < resultNum; i++) {
                T t = copyList.remove(random.nextInt(copyList.size()));
                result.add(t);
            }
        }
        return result;
    }

/*    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }

        List<String> resule = pick(list, 10);
        resule.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        });
        System.out.println(resule);
    }*/
}
