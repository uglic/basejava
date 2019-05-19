package ru.javawebinar.basejava.util;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.model.AbstractSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SimpleTextSection;

import static ru.javawebinar.basejava.TestData.RESUME_EXIST_1;

public class JsonParserTest {
    @Test
    public void testResume() throws Exception {
        String json = JsonParser.write(RESUME_EXIST_1);
        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);
        Assert.assertEquals(RESUME_EXIST_1, resume);
    }

    @Test
    public void write() throws Exception {
        AbstractSection section1 = new SimpleTextSection("Objective1");
        String json = JsonParser.write(section1, AbstractSection.class);
        System.out.println(json);
        AbstractSection section2 = JsonParser.read(json, AbstractSection.class);
        Assert.assertEquals(section1, section2);
    }
}
