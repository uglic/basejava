package ru.javawebinar.basejava.generator.param;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

public class LoginGeneratorParam implements IGeneratorParameter {
    private final boolean isMan;
    private final String login;

    public LoginGeneratorParam(boolean isMan, String login) {
        this.isMan = isMan;
        this.login = login;
    }

    @Override
    public Boolean isMan() {
        return isMan;
    }

    @Override
    public String getLogin() {
        return login;
    }
}
