package ru.javawebinar.basejava.generator.contact;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.IRandomDataGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

public class SkypeGenerator implements IRandomDataGenerator<String> {
    private static volatile SkypeGenerator instance;

    private SkypeGenerator() {
    }

    public static SkypeGenerator getInstance() {
        if (instance == null) {
            synchronized (SkypeGenerator.class) {
                if (instance == null) {
                    instance = new SkypeGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public String getRandom(IGeneratorParameter gp) {
        return "live:" + gp.getLogin();
    }
}
