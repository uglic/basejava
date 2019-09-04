package ru.javawebinar.basejava.generator;

import org.junit.jupiter.api.Test;

import java.util.List;

class GeneratedDataTest {

    @Test
    void getMenNames() {
        List<String> list = GeneratedData.getMenNames();
        System.out.println(list);
    }

    @Test
    void getWomenNames() {
        List<String> list = GeneratedData.getWomenNames();
        System.out.println(list);
    }

    @Test
    void getMenPatronymics() {
        List<String> list = GeneratedData.getMenPatronymics();
        System.out.println(list);
    }
}