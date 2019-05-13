package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HW12OddOrEven {
    static List<Integer> oddOrEven(List<Integer> integers) {
        int remainder = integers.stream().reduce(Integer::sum).orElse(0) % 2;
        //Predicate<Integer> test = i -> i % 2 == (1 - remainder);
        Predicate<Integer> test = i -> i % 2 != remainder;
        return integers.stream().filter(test).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Integer> values;

        values = new ArrayList<>(Arrays.asList(1, 2, 3, 3, 2, 3));
        System.out.println("oddOrEven for " + values + " (sum=" + values.stream().mapToInt(i -> i).sum() + ") is " + oddOrEven(values));

        values = new ArrayList<>(Arrays.asList(2, 4, 5, 7, 9, 6));
        System.out.println("oddOrEven for " + values + " (sum=" + values.stream().mapToInt(i -> i).sum() + ") is " + oddOrEven(values));
    }
}
