package ru.netology.shop;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginWithActiveUser() {
        // Регистрируем активного пользователя через API
        RegistrationDto activeUser = new RegistrationDto("vasya", "qwerty123", "active");
        ApiHelper.registerUser(activeUser);

        // Выполняем вход через UI
        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем успешный вход
        $("[data-test-id=dashboard]").shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Личный кабинет"));
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        // Регистрируем заблокированного пользователя
        RegistrationDto blockedUser = new RegistrationDto("petya", "qwerty123", "blocked");
        ApiHelper.registerUser(blockedUser);

        // Пытаемся войти
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем сообщение об ошибке
        $("[data-test-id=error-notification]")
                .shouldBe(visible, Duration.ofSeconds(5))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        // Регистрируем активного пользователя
        RegistrationDto validUser = new RegistrationDto("vasya", "qwerty123", "active");
        ApiHelper.registerUser(validUser);

        // Пытаемся войти с неверным логином
        $("[data-test-id=login] input").setValue("invalid_login");
        $("[data-test-id=password] input").setValue(validUser.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем ошибку
        $("[data-test-id=error-notification]")
                .shouldBe(visible, Duration.ofSeconds(5))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        // Регистрируем активного пользователя
        RegistrationDto validUser = new RegistrationDto("vasya", "qwerty123", "active");
        ApiHelper.registerUser(validUser);

        // Пытаемся войти с неверным паролем
        $("[data-test-id=login] input").setValue(validUser.getLogin());
        $("[data-test-id=password] input").setValue("wrong_password");
        $("[data-test-id=action-login]").click();

        // Проверяем ошибку
        $("[data-test-id=error-notification]")
                .shouldBe(visible, Duration.ofSeconds(5))
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}