package ru.javawebinar.basejava.generator;

import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.generator.param.IsManGeneratorParam;
import ru.javawebinar.basejava.model.Resume;

class ResumeGeneratorTest {

    @Test
    void getResumeRandom() {
        Resume resume = ResumeGenerator.getInstance().getRandom(new IsManGeneratorParam(true));
        System.out.println(resume);
    }
}