package ru.netology;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private static Faker faker;
    private static final Random random = new Random();

    private DataGenerator() {
    }

    public static int generateIntInRange(int begin, int end) {
        return random.nextInt(end + 1 - begin) + begin;
    }

    public static String generateRandomSymbols(int countSymbols) {
        String symbols = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*-+0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < countSymbols; i++){
            result.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        return result.toString();
    }
    public static String generateLogin(String locale) {
        return generateFirstName(locale) + generateLastName(locale);
    }

    public static String generateFirstName(String locale) {
        faker = new Faker(new Locale(locale));
        return faker.name().firstName().replaceAll("ё", "е").replaceAll("Ё", "Е");
    }

    public static String generateLastName(String locale) {
        faker = new Faker(new Locale(locale));
        return faker.name().lastName().replaceAll("ё", "е").replaceAll("Ё", "Е");
    }

    public static String generateEmail() {
        faker = new Faker(new Locale("en"));
        return faker.internet().emailAddress();
    }

    public static String generatePassword(int lengthIs8AndMore) {
        if (lengthIs8AndMore < 8) {
            lengthIs8AndMore = 8;
        }
        StringBuilder password = new StringBuilder();
        String lowerLetters = "qwertyuiopasdfghjklzxcvbnm";
        String upperLetters = lowerLetters.toUpperCase();
        String digitChars = "1234567890";
        String specChars = "!?@#$%^&*-+";
        String allLetters = lowerLetters + upperLetters;
        String allChars = lowerLetters + upperLetters + digitChars + specChars;

        char[] charArray = new char[lengthIs8AndMore - 4];

        //first two symbols are any letters
        for(int i = 0; i < 2; i++){
            password.append(allLetters.charAt(random.nextInt(allLetters.length())));
        }

        //next two symbols are lower letters
        for(int i = 2; i < 4; i++){
            password.append(lowerLetters.charAt(random.nextInt(lowerLetters.length())));
        }

        //must have a spec symbol, a digit and an upper letter
        charArray[0] = specChars.charAt(random.nextInt(specChars.length()));
        charArray[1] = digitChars.charAt(random.nextInt(digitChars.length()));
        charArray[2] = upperLetters.charAt(random.nextInt(upperLetters.length()));

        //everything else is filled with any symbols
        for(int i = 3; i < charArray.length; i++){
            charArray[i] = allChars.charAt(random.nextInt(allChars.length()));
        }

        //mixing the element in the charArray
        for (int i = 0; i < lengthIs8AndMore - 4; i++) {
            int numberOfRandomSymbolInCharArray = random.nextInt(charArray.length);
            password.append(charArray[numberOfRandomSymbolInCharArray]);
            char[] tempForDeleteUsedChar = new char[charArray.length - 1];
            int numberInTemp = 0;

            //delete used element from the charArray
            for(int j = 0; j < charArray.length; j++){
                if(j != numberOfRandomSymbolInCharArray){
                    tempForDeleteUsedChar[numberInTemp] = charArray[j];
                    numberInTemp++;
                }
            }
            charArray = tempForDeleteUsedChar;
        }
        return password.toString();
    }
    public static String generatePassword() {
        return generatePassword(8);
    }
}
