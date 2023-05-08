package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static ru.netology.DataGenerator.*;

class PatternsTask2Test {
    private static final RegistrationDto activeUser = new RegistrationDto(
            generateLogin("en"),
            generatePassword(generateIntInRange(8, 15)),
            "active");

    private static final RegistrationDto blockedUser = new RegistrationDto(
            generateLogin("en"),
            generatePassword(generateIntInRange(8, 15)),
            "blocked");

    SelenideElement loginInput = $("input[name=login]");
    SelenideElement loginErrorField = $("span[data-test-id=login] span.input__sub");
    SelenideElement passwordInput = $("input[name=password]");
    SelenideElement passwordErrorField = $("span[data-test-id=password] span.input__sub");
    SelenideElement continueButton = $("button[data-test-id='action-login']");
    SelenideElement personPageName = $("h2");
    SelenideElement errorPopUp = $("div[data-test-id='error-notification'] div.notification__content");

    String titleName = "  Личный кабинет";
    String errorMassage = "Неверно указан логин или пароль";
    String blockedUserMassage = "Пользователь заблокирован";
    String emptyFieldMassage = "Поле обязательно для заполнения";



    // спецификация нужна для того, чтобы переиспользовать настройки в разных запросах
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUpAll() {
        given()
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(activeUser) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then()
                .statusCode(200);

        given()
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(blockedUser) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then()
                .statusCode(200);
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }


    @Test
    public void testValidDataForRegistratedUser() {
        loginInput.setValue(activeUser.getLogin());
        passwordInput.setValue(activeUser.getPassword());
        continueButton.click();

        personPageName
                .shouldHave(Condition.text(titleName), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
    }

    @Test
    public void testValidDataForBlockedUser() {
        loginInput.setValue(blockedUser.getLogin());
        passwordInput.setValue(blockedUser.getPassword());
        continueButton.click();

        errorPopUp
                .shouldHave(Condition.text(blockedUserMassage), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
    }

    @Test
    public void testNotRegistratedUser() {
        String userLogin = generateLogin("en");
        String userPassword = generatePassword();

        loginInput.setValue(userLogin);
        passwordInput.setValue(userPassword);
        continueButton.click();

        errorPopUp
                .shouldHave(Condition.text(errorMassage), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
    }

    @Test
    public void testInvalidPasswordForRegistratedUser() {
        loginInput.setValue(activeUser.getLogin());
        passwordInput.setValue(activeUser.getPassword() + generateRandomSymbols(generateIntInRange(1,5)));
        continueButton.click();

        errorPopUp
                .shouldHave(Condition.text(errorMassage), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
    }

    @Test
    public void testInvalidLoginForRegistratedUser() {
        loginInput.setValue(activeUser.getLogin() + generateFirstName("en"));
        passwordInput.setValue(activeUser.getPassword());
        continueButton.click();

        errorPopUp
                .shouldHave(Condition.text(errorMassage), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
    }

    @Test
    public void testInvalidPasswordForBlockedUser() {
        loginInput.setValue(blockedUser.getLogin());
        passwordInput.setValue(blockedUser.getPassword() + generateRandomSymbols(2));
        continueButton.click();

        errorPopUp
                .shouldHave(Condition.text(errorMassage), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
    }

    @Test
    public void testValidLoginEmptyPasswordForRegistratedUser() {
        loginInput.setValue(activeUser.getLogin());

        continueButton.click();

        passwordErrorField
                .shouldHave(Condition.text(emptyFieldMassage), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
    }

    @Test
    public void testEmptyLoginValidPasswordForRegistratedUser() {

        passwordInput.setValue(activeUser.getPassword());
        continueButton.click();

        loginErrorField
                .shouldHave(Condition.text(emptyFieldMassage), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
    }

    @Test
    public void testEmptyForm() {

        continueButton.click();

        loginErrorField
                .shouldHave(Condition.text(emptyFieldMassage), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
        passwordErrorField
                .shouldHave(Condition.text(emptyFieldMassage), Duration.ofSeconds(15))
                .shouldBe(Condition.appear);
    }
}