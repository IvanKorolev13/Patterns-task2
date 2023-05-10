package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static Faker faker;
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    public static void setUser(RegistrationDto user) {
        given()
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then()
                .statusCode(200);
    }

    public static String generateRandomSymbols() {
        return faker.letterify("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*-+0123456789");
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

    public static String generatePassword() {
        return faker.internet().password();
    }
}
