package ru.netology.shop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameField = By.name("login");
    private final By passwordField = By.name("password");
    private final By loginButton = By.cssSelector("[data-test-id='action-login']");
    private final By errorMessage = By.cssSelector("[data-test-id='error-notification']");
    private final By loader = By.cssSelector(".spin"); // Локатор для индикатора загрузки

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void login(String username, String password) {
        // Ожидаем появления полей
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        usernameElement.clear();
        usernameElement.sendKeys(username);

        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordElement.clear();
        passwordElement.sendKeys(password);

        // Нажимаем кнопку
        WebElement loginButtonElement = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButtonElement.click();

        // Ожидаем исчезновения индикатора загрузки (если появляется)
        waitForLoaderToDisappear();
    }

    public boolean isErrorDisplayed() {
        try {
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return errorElement.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return errorElement.getText();
        } catch (TimeoutException e) {
            return "Error message not found";
        }
    }

    private void waitForLoaderToDisappear() {
        try {
            // Проверяем, появляется ли индикатор загрузки
            wait.until(ExpectedConditions.visibilityOfElementLocated(loader));

            // Если появился - ждем его исчезновения
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
        } catch (TimeoutException e) {
            // Если не появился - продолжаем без ожидания
        }
    }
}