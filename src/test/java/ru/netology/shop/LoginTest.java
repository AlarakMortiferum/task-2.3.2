package ru.netology.shop;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.shop.pages.LoginPage;
import ru.netology.testmode.data.DataHelper;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @BeforeAll
    static void setUpAll() {
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.headless = true;
        Configuration.holdBrowserOpen = false;
        Configuration.timeout = 5000;
    }

    @Test
    void shouldLoginActiveUser() {
        var user = DataHelper.getRegisteredActiveUser();
        open("/login");
        new LoginPage()
                .login(user.getLogin(), user.getPassword())
                .shouldSeeDashboard();
    }

    @Test
    void shouldNotLoginBlockedUser() {
        var user = DataHelper.getRegisteredBlockedUser();
        open("/login");
        new LoginPage()
                .login(user.getLogin(), user.getPassword())
                .shouldSeeError("Ошибка! Пользователь заблокирован");
    }

    @Test
    void shouldNotLoginUnregisteredUser() {
        var user = DataHelper.getUnregisteredUser();
        open("/login");
        new LoginPage()
                .login(user.getLogin(), user.getPassword())
                .shouldSeeError("Ошибка! Неверно указан логин или пароль");
    }
}
