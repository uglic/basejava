package ru.javawebinar.basejava.generator.param;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.model.SectionTypes;

public class SectionTypeGeneratorParam implements IGeneratorParameter {
    private final boolean isMan;
    private final SectionTypes sectionType;

    public SectionTypeGeneratorParam(boolean isMan, SectionTypes sectionType) {
        this.isMan = isMan;
        this.sectionType = sectionType;
    }

    @Override
    public Boolean isMan() {
        return isMan;
    }

    @Override
    public SectionTypes getSectionType() {
        return sectionType;
    }
}
