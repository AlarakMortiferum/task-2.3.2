package ru.netology.testmode.test;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.testmode.data.DataGenerator.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AuthTest {

    @Test
    void shouldLoginSuccessfullyWithActiveUser() {
        User user = DataGenerator.getActiveUser();

        // Авторизация
        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("http://localhost:9999/api/auth")
                .then()
                .statusCode(200);

        // Получение кода и верификация
        var code = DataGenerator.getVerificationCode(user);

        given()
                .contentType(ContentType.JSON)
                .body(new DataGenerator.Verification(user.login, code))
                .when()
                .post("http://localhost:9999/api/auth/verification")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        User user = DataGenerator.getBlockedUser();

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("http://localhost:9999/api/auth")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        User user = DataGenerator.getUserWithWrongLogin();

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("http://localhost:9999/api/auth")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        User user = DataGenerator.getUserWithWrongPassword();

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("http://localhost:9999/api/auth")
                .then()
                .statusCode(401);
    }
}
