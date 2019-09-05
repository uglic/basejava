package ru.javawebinar.basejava.generator.param;

import ru.javawebinar.basejava.model.ContactTypes;

public class ContactTypeGeneratorParam implements IGeneratorParameter {
    private final boolean isMan;
    private final ContactTypes contactTypes;

    public ContactTypeGeneratorParam(boolean isMan, ContactTypes contactTypes) {
        this.isMan = isMan;
        this.contactTypes = contactTypes;
    }

    @Override
    public Boolean isMan() {
        return isMan;
    }

    @Override
    public ContactTypes getContactType() {
        return contactTypes;
    }
}
