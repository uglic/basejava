package ru.javawebinar.basejava;

import java.util.Arrays;

public class HW12MinValue {
    static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce((s, i) -> s * 10 + i)
                .orElse(0);
    }

    static String printIntArray(int[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int value : values) {
            if (sb.length() > 1) sb.append(",");
            sb.append(value);
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        int[] values;
        int result;

        values = new int[]{1, 2, 3, 3, 2, 3};
        System.out.println("minValue for " + printIntArray(values) + " is " + minValue(values));

        values = new int[]{9, 1};
        System.out.println("minValue for " + printIntArray(values) + " is " + minValue(values));

        values = new int[]{0};
        System.out.println("minValue for " + printIntArray(values) + " is " + minValue(values));

        values = new int[]{};
        System.out.println("minValue for " + printIntArray(values) + " is " + minValue(values));

        values = new int[]{1, 1, 1, 1, 1, 3, 3, 3, 4, 5, 6, 7, 8, 9};
        System.out.println("minValue for " + printIntArray(values) + " is " + minValue(values));
    }
}
