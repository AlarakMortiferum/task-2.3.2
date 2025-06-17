package patterns.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {
    private SelenideElement loginField = $(byName("login"));
    private SelenideElement passwordField = $(byName("password"));
    private SelenideElement submitButton = $("button[type='button']");

    public void openPage() {
        open("http://localhost:9999");
    }

    public void login(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        submitButton.click();
    }

    public void shouldSeeDashboard() {
        $("h2").shouldBe(visible).shouldHave(text("Личный кабинет"));
    }

    public void shouldSeeError(String message) {
        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text(message));
    }
}