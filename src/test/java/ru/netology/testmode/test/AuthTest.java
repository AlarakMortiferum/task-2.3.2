package ru.netology.testmode.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataHelper.User;
import ru.netology.testmode.data.DataHelper;

import static io.restassured.RestAssured.given;

public class AuthTest {

    private static RequestSpecification spec;

    @BeforeAll
    static void setUpAll() {
        spec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    void shouldLoginSuccessfullyWithActiveUser() {
        User user = DataHelper.getRegisteredActiveUser(); // регистрируем через API

        given()
                .spec(spec)
                .body(user)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        User user = DataHelper.getRegisteredBlockedUser();

        given()
                .spec(spec)
                .body(user)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        User user = DataHelper.getWrongLoginUser(); // корректный пароль, но логин не зарегистрирован

        given()
                .spec(spec)
                .body(user)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        User user = DataHelper.getWrongPasswordUser(); // корректный логин, но неверный пароль

        given()
                .spec(spec)
                .body(user)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(401);
    }
}
