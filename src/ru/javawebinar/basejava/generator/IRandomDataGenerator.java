package ru.javawebinar.basejava.generator;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

@FunctionalInterface
public interface IRandomDataGenerator<T> {
    T getRandom(IGeneratorParameter parameter);
}
