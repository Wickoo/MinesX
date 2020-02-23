package com.github.wickoo.minesx.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {

    public static String col (String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String locToString (Location location) { return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ(); }

    public static Double getHigherNum (double num1, double num2) {
        if (num1 > num2 ) {
            return num1;
        }
        return num2;
    }

    public static Double getLowerNum (double num1, double num2) {
        if (num1 < num2 ) {
            return num1;
        }
        return num2;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String capitalizeWord (String str) {
        String[] words = str.split("\\s");
        String capitalizeWord = "";
        for (String w : words) {

            String first = w.substring(0,1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase()+afterfirst + " ";

        }

        return capitalizeWord.trim();

    }

}

