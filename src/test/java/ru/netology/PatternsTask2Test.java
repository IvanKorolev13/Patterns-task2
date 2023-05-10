package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.DataGenerator.*;

class PatternsTask2Test {
    private static final RegistrationDto activeUser = new RegistrationDto(
            generateLogin("en"),
            generatePassword(),
            "active");

    private static final RegistrationDto blockedUser = new RegistrationDto(
            generateLogin("en"),
            generatePassword(),
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

    @BeforeAll
    static void setUpAll() {
        setUser(activeUser);
        setUser(blockedUser);
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
        passwordInput.setValue(activeUser.getPassword() + generateRandomSymbols());
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
        passwordInput.setValue(blockedUser.getPassword() + generateRandomSymbols());
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