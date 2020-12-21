package com.antel.picker.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyWorlFilter {

    public static String keywordFilter(String content, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        System.out.println(m);
        ArrayList list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return null;
    }

    public static void main(String[] args) {
        String content = "今天天气ag不m错，挺风和09764日丽的不错";
        String pattern = "[0-9a-zA-Z]";

        String s = keywordFilter(content, pattern);
        System.out.println(s);
    }

}
