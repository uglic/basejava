package ru.javawebinar.basejava.generator;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

class FullNameGeneratorTest {

    @Test
    void printRandomMenFullNames() {
        printMultiThreadRandomFullNames(true, 500);
    }

    @Test
    void printRandomWomenFullNames() {
        printMultiThreadRandomFullNames(false, 500);
    }

    private void printMultiThreadRandomFullNames(boolean isMan, int count) {
        final IGeneratorParameter parameter = () -> true;
        Thread[] tasks = new Thread[count];
        for (int i = 0; i < count; i++) {
            final int id = i;
            tasks[id] = new Thread(() -> {
                String name = FullNameGenerator.getInstance().getRandom(parameter);
                System.out.format("Task %03d: %s\n", id, name);
            });
        }
        for (Thread thread : tasks) {
            thread.start();
        }
    }
}