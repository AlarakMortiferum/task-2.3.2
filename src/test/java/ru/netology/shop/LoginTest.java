package ru.netology.shop;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import patterns.pages.LoginPage;
import ru.netology.testmode.data.DataHelper;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @BeforeAll
    static void setUpAll() {
        // Базовый URL и порт
        Configuration.baseUrl = "http://localhost";
        Configuration.port = 9999;
        // Чтобы браузер не открывался визуально
        Configuration.headless = true;
        // Не держать окно открытым после теста
        Configuration.holdBrowserOpen = false;
        // Короткие таймауты для быстрого фейла
        Configuration.timeout = 5000;
    }

    @Test
    void shouldLoginActiveUser() {
        var user = DataHelper.getActiveUser();

        open("/login");
        new LoginPage()
                .login(user.getLogin(), user.getPassword())
                .shouldSeeDashboard();
    }

    @Test
    void shouldNotLoginBlockedUser() {
        var user = DataHelper.getBlockedUser();

        open("/login");
        new LoginPage()
                .login(user.getLogin(), user.getPassword())
                .shouldSeeError("Ошибка! Пользователь заблокирован");
    }

    @Test
    void shouldNotLoginUnregisteredUser() {
        var user = DataHelper.getRandomUser();

        open("/login");
        new LoginPage()
                .login(user.getLogin(), user.getPassword())
                .shouldSeeError("Ошибка! Неверно указан логин или пароль");
    }
}
