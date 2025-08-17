package ru.netology.shop;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.shop.dto.RegistrationDto;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    private static final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri("http://localhost") // Исправлено setBaseUri вместо setBaseUrl
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void registerUser(RegistrationDto user) {
        try {
            given()
                    .spec(spec)
                    .body(user)
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
        } catch (Exception e) {
            System.err.println("Ошибка регистрации пользователя: " + e.getMessage());
        }
    }
}