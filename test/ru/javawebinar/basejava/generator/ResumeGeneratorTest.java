package ru.javawebinar.basejava.generator;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.generator.param.IsManGeneratorParam;
import ru.javawebinar.basejava.model.Resume;

class ResumeGeneratorTest {

    @Test
    void getResumeRandom() {
        for (int i = 0; i < 10; i++) {
            Resume resume = ResumeGenerator.getInstance().getRandom(new IsManGeneratorParam(true));
            System.out.println(resume);
        }
    }
}