package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HW12OddOrEven {
    static List<Integer> oddOrEven(List<Integer> integers) {
        //if (integers.stream().mapToInt(i -> i).sum() % 2 == 0) {
        if (integers.stream().reduce(Integer::sum).orElse(0) % 2 == 0) {
            return integers.stream().filter(i -> i % 2 == 1).collect(Collectors.toList());
        } else {
            return integers.stream().filter(i -> i % 2 == 0).collect(Collectors.toList());
        }
    }

    public static void main(String[] args) {
        List<Integer> values;

        values = new ArrayList<>(Arrays.asList(1, 2, 3, 3, 2, 3));
        System.out.println("oddOrEven for " + values + " (sum=" + values.stream().mapToInt(i -> i).sum() + ") is " + oddOrEven(values));

        values = new ArrayList<>(Arrays.asList(2, 4, 5, 7, 9, 6));
        System.out.println("oddOrEven for " + values + " (sum=" + values.stream().mapToInt(i -> i).sum() + ") is " + oddOrEven(values));
    }
}
