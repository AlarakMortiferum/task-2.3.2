package ru.netology.testmode.test;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.testmode.data.DataGenerator.User;

import static io.restassured.RestAssured.given;

public class AuthTest {

    @Test
    void shouldLoginSuccessfullyWithActiveUser() {
        User user = DataGenerator.getActiveUser();

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("http://localhost:9999/api/auth")
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
