package ru.netology.shop;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.apache.commons.io.FileUtils;
import ru.netology.shop.pages.LoginPage;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private static final String BASE_URL = "http://localhost:9999";

    @BeforeAll
    static void setupAll() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sadim\\Downloads\\chromedriver-win32\\chromedriver-win32\\chromedriver.exe");
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--headless=new");

        driver = new ChromeDriver(options);

        // Настройка неявных ожиданий
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        // Открываем страницу входа
        driver.get(BASE_URL + "/login");

        // Проверяем, что страница загрузилась
        if (!driver.getTitle().contains("Регистрация") && !driver.getTitle().contains("AQA")) {
            takeScreenshot("page_load_failure");
            fail("Страница входа не загрузилась. Title: " + driver.getTitle());
        }

        loginPage = new LoginPage(driver);
    }

    @Test
    @DisplayName("Успешный вход в систему")
    void shouldLoginWithValidCredentials() {
        loginPage.login("vasya", "qwerty123");

        // Проверяем, что произошло перенаправление
        assertNotEquals(
                BASE_URL + "/login",
                driver.getCurrentUrl(),
                "После успешного входа URL должен измениться"
        );

        // Проверяем, что мы на нужной странице (dashboard или cards)
        assertTrue(
                driver.getCurrentUrl().contains("/dashboard") ||
                        driver.getCurrentUrl().contains("/cards"),
                "После входа пользователь должен быть перенаправлен на страницу карт или dashboard"
        );
    }

    @Test
    @DisplayName("Ошибка входа с неверными учетными данными")
    void shouldShowErrorWithInvalidCredentials() {
        loginPage.login("invalid", "invalid");

        // Проверяем сообщение об ошибке
        assertTrue(
                loginPage.isErrorDisplayed(),
                "Должно отображаться сообщение об ошибке"
        );

        // Проверяем текст ошибки (если известен ожидаемый текст)
        String errorText = loginPage.getErrorMessageText();
        assertTrue(
                errorText.contains("Ошибка") || errorText.contains("Неверно") || errorText.contains("Invalid"),
                "Текст ошибки должен содержать информацию о проблеме. Актуальный текст: " + errorText
        );

        // Проверяем, что URL не изменился
        assertEquals(
                BASE_URL + "/login",
                driver.getCurrentUrl(),
                "При ошибке входа URL должен остаться прежним"
        );
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            takeScreenshot("after_test");
            driver.quit();
        }
    }

    private void takeScreenshot(String name) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File("screenshots/" + name + ".png"));
        } catch (IOException e) {
            System.err.println("Не удалось сохранить скриншот: " + e.getMessage());
        }
    }
}