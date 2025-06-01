package ru.netology.testmode.test;

import org.junit.jupiter.api.*;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.testmode.data.DataGenerator.User;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class AuthTest {

    @BeforeEach
    void openPage() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginWithActiveUser() {
        User user = DataGenerator.getActiveUser();

        $("[data-test-id=login] input").setValue(user.login);
        $("[data-test-id=password] input").setValue(user.password);
        $("[data-test-id=action-login]").click();

        $("[data-test-id=dashboard]").shouldBe(visible);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        User user = DataGenerator.getBlockedUser();

        $("[data-test-id=login] input").setValue(user.login);
        $("[data-test-id=password] input").setValue(user.password);
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        User user = DataGenerator.getUserWithWrongLogin();

        $("[data-test-id=login] input").setValue(user.login);
        $("[data-test-id=password] input").setValue(user.password);
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        User user = DataGenerator.getUserWithWrongPassword();

        $("[data-test-id=login] input").setValue(user.login);
        $("[data-test-id=password] input").setValue(user.password);
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}
