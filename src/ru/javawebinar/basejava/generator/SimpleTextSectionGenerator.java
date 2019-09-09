package ru.javawebinar.basejava.generator;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.generator.param.IsManGeneratorParam;
import ru.javawebinar.basejava.model.SimpleTextSection;

public class SimpleTextSectionGenerator implements IRandomDataGenerator<SimpleTextSection> {
    private static volatile SimpleTextSectionGenerator instance;
    private final IRandomDataGenerator<String> textGenerator = ObjectiveGenerator.getInstance();

    private SimpleTextSectionGenerator() {
    }

    public static SimpleTextSectionGenerator getInstance() {
        if (instance == null) {
            synchronized (SimpleTextSectionGenerator.class) {
                if (instance == null) {
                    instance = new SimpleTextSectionGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public SimpleTextSection getRandom(IGeneratorParameter parameter) {
        return new SimpleTextSection(textGenerator.getRandom(new IsManGeneratorParam(parameter.isMan())));
    }
}
