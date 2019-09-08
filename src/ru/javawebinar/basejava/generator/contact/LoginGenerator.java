package ru.javawebinar.basejava.generator.contact;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.IRandomDataGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

import java.util.concurrent.ThreadLocalRandom;

public class LoginGenerator implements IRandomDataGenerator<String> {
    private static volatile LoginGenerator instance;

    private LoginGenerator() {
    }

    public static LoginGenerator getInstance() {
        if (instance == null) {
            synchronized (LoginGenerator.class) {
                if (instance == null) {
                    instance = new LoginGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public String getRandom(IGeneratorParameter gp) {
        boolean isMan = gp.isMan();
        String fullName = gp.getFullName();
        String[] parts = fullName.split(" ");
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int[] indexes = getRandomIndexes(random);
        String login = filter(parts[indexes[0]], 1, 4,
                random.nextBoolean(), random);
        login = login.concat(filter(parts[indexes[1]], 1, 4,
                !isVowel(login.charAt(login.length() - 1)), random));
        return login.concat(filter(parts[indexes[2]], 1, 4,
                !isVowel(login.charAt(login.length() - 1)), random));
    }

    private int[] getRandomIndexes(ThreadLocalRandom random) {
        int indexes[] = new int[3];
        switch (random.nextInt(6)) {
            case 0:
                indexes[0] = 0;
                indexes[1] = 1;
                indexes[2] = 2;
                break;
            case 1:
                indexes[0] = 1;
                indexes[1] = 0;
                indexes[2] = 2;
                break;
            case 2:
                indexes[0] = 1;
                indexes[1] = 2;
                indexes[2] = 0;
                break;
            case 3:
                indexes[0] = 0;
                indexes[1] = 2;
                indexes[2] = 1;
                break;
            case 4:
                indexes[0] = 2;
                indexes[1] = 0;
                indexes[2] = 1;
                break;
            case 5:
                indexes[0] = 2;
                indexes[1] = 1;
                indexes[2] = 0;
                break;
        }
        return indexes;
    }

    private String filter(String inPart, int minLetters, int maxLetters, boolean beginWithVowel, ThreadLocalRandom random) {
        int len = random.nextInt(minLetters, maxLetters + 1);
        StringBuilder sb = new StringBuilder();
        int position = 0;
        boolean prevIsVowel = !beginWithVowel;
        while (sb.length() < len && position < inPart.length()) {
            char ch = inPart.charAt(position);
            if ((isVowel(ch) && !prevIsVowel) || (!isVowel(ch) && prevIsVowel)) {
                sb.append(toRoman(ch));
                prevIsVowel = !prevIsVowel;
            }
            position++;
        }
        return sb.toString();
    }

    private String toRoman(char lowCaseRussianChar) {
        String result;
        switch (Character.toLowerCase(lowCaseRussianChar)) {
            case 'а':
                result = "a";
                break;
            case 'б':
                result = "b";
                break;
            case 'в':
                result = "v";
                break;
            case 'г':
                result = "g";
                break;
            case 'д':
                result = "d";
                break;
            case 'е':
                result = "e";
                break;
            case 'ё':
                result = "x";
                break;
            case 'ж':
                result = "j";
                break;
            case 'з':
                result = "z";
                break;
            case 'и':
                result = "i";
                break;
            case 'й':
                result = "y";
                break;
            case 'к':
                result = "k";
                break;
            case 'л':
                result = "l";
                break;
            case 'м':
                result = "m";
                break;
            case 'н':
                result = "n";
                break;
            case 'о':
                result = "o";
                break;
            case 'п':
                result = "p";
                break;
            case 'р':
                result = "r";
                break;
            case 'с':
                result = "s";
                break;
            case 'т':
                result = "t";
                break;
            case 'у':
                result = "u";
                break;
            case 'ф':
                result = "f";
                break;
            case 'х':
                result = "wh";
                break;
            case 'ц':
                result = "c";
                break;
            case 'ч':
                result = "ch";
                break;
            case 'ш':
                result = "sh";
                break;
            case 'щ':
                result = "shch";
                break;
            case 'ъ':
                result = "y";
                break;
            case 'ы':
                result = "ee";
                break;
            case 'ь':
                result = "q";
                break;
            case 'э':
                result = "e";
                break;
            case 'ю':
                result = "u";
                break;
            case 'я':
                result = "ya";
                break;
            default:
                result = "";
        }
        return result;
    }

    private boolean isVowel(char ch) {
        switch (Character.toLowerCase(ch)) {
            // russian
            case 'а':
            case 'я':
            case 'о':
            case 'ё':
            case 'э':
            case 'е':
            case 'у':
            case 'ю':
            case 'ы':
            case 'и':
                // roman
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                return true;
            default:
                return false;
        }
    }
}
