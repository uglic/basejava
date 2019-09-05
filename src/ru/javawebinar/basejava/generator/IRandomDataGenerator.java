package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

@FunctionalInterface
public interface IRandomDataGenerator<T> {
    T getRandom(IGeneratorParameter parameter);
}
