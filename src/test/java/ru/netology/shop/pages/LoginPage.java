package ru.netology.shop.pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.*;

public class LoginPage {

    public LoginPage login(String login, String password) {
        $("[data-test-id=login] input").setValue(login);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();
        return this;
    }

    public LoginPage shouldSeeDashboard() {
        $("h2").shouldHave(text("Личный кабинет"));
        return this;
    }

    public LoginPage shouldSeeError(String message) {
        $(".notification__content").shouldHave(text(message));
        return this;
    }
}
