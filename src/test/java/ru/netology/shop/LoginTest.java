package ru.netology.shop;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.shop.data.DataHelper;
import ru.netology.shop.dto.RegistrationDto;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginWithActiveUser() {
        RegistrationDto activeUser = DataHelper.getActiveUser();
        ApiHelper.registerUser(activeUser);

        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=dashboard]").shouldBe(visible)
                .shouldHave(text("Личный кабинет"));
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
                .shouldHave(text("Неверные данные для входа"));
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        RegistrationDto validUser = DataHelper.getActiveUser();
        ApiHelper.registerUser(validUser);

        $("[data-test-id=login] input").setValue(DataHelper.getInvalidLogin());
        $("[data-test-id=password] input").setValue(validUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Неверные данные для входа"));
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        RegistrationDto validUser = DataHelper.getActiveUser();
        ApiHelper.registerUser(validUser);

        $("[data-test-id=login] input").setValue(validUser.getLogin());
        $("[data-test-id=password] input").setValue(DataHelper.getInvalidPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Неверные данные для входа"));
    }
}