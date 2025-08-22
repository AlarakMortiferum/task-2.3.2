package ru.netology.shop;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import ru.netology.shop.data.DataHelper;
import ru.netology.shop.dto.RegistrationDto;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterEach
    void cleanup() {
        closeWebDriver();
    }

    @Test
    void shouldLoginWithActiveUser() {
        RegistrationDto activeUser = DataHelper.getActiveUser();
        ApiHelper.registerUser(activeUser);

        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldNotBe(visible);
        assertFalse(WebDriverRunner.url().contains("login"), "URL не должен содержать 'login'");
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        RegistrationDto blockedUser = DataHelper.getBlockedUser();
        ApiHelper.registerUser(blockedUser);

        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        RegistrationDto validUser = DataHelper.getActiveUser();
        ApiHelper.registerUser(validUser);

        $("[data-test-id=login] input").setValue(DataHelper.generateRandomLogin());
        $("[data-test-id=password] input").setValue(validUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        RegistrationDto validUser = DataHelper.getActiveUser();
        ApiHelper.registerUser(validUser);

        $("[data-test-id=login] input").setValue(validUser.getLogin());
        $("[data-test-id=password] input").setValue(DataHelper.generateRandomPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}