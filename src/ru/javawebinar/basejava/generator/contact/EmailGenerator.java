package ru.javawebinar.basejava.generator.contact;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.IRandomDataGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

import java.util.concurrent.ThreadLocalRandom;

public class EmailGenerator implements IRandomDataGenerator<String> {
    private static volatile EmailGenerator instance;

    private EmailGenerator() {
    }

    public static EmailGenerator getInstance() {
        if (instance == null) {
            synchronized (EmailGenerator.class) {
                if (instance == null) {
                    instance = new EmailGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public String getRandom(IGeneratorParameter gp) {
        String login = gp.getLogin() + "@";
        switch (ThreadLocalRandom.current().nextInt(6)) {
            case 0:
                login = login.concat("yandex.ru");
                break;
            case 1:
                login = login.concat("gmail.com");
                break;
            case 2:
                login = login.concat("vfemail.net");
                break;
            case 3:
                login = login.concat("mail.ru");
                break;
            case 4:
                login = login.concat("rbcmail.ru");
                break;
            case 5:
                login = login.concat("rambler.ru");
                break;
        }
        return login;
    }
}
