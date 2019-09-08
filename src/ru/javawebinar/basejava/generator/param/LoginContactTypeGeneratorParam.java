package ru.javawebinar.basejava.generator.param;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.model.ContactTypes;

public class LoginContactTypeGeneratorParam implements IGeneratorParameter {
    private final boolean isMan;
    private final ContactTypes contactTypes;
    private final String login;

    public LoginContactTypeGeneratorParam(boolean isMan, ContactTypes contactTypes, String login) {
        this.isMan = isMan;
        this.contactTypes = contactTypes;
        this.login = login;
    }

    @Override
    public Boolean isMan() {
        return isMan;
    }

    @Override
    public ContactTypes getContactType() {
        return contactTypes;
    }

    @Override
    public String getLogin() {
        return login;
    }
}
