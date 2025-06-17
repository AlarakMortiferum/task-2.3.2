package ru.netology.testmode.data;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class DataHelper {

    private static final String BASE_URI = "http://localhost";
    private static final int PORT = 9999;
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setPort(PORT)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(io.restassured.filter.log.LogDetail.ALL)
            .build();
    private static final Gson gson = new Gson();
    private static final Random rnd = new Random();

    // DTO для передачи в теле
    public static class RegistrationDto {
        private final String login;
        private final String password;
        private final String status;

        public RegistrationDto(String login, String password, String status) {
            this.login = login;
            this.password = password;
            this.status = status;
        }
    }

    // Класс, который вернётся в тесты
    public static class User {
        private final String login;
        private final String password;

        public User(String login, String password) {
            this.login = login;
            this.password = password;
        }
        public String getLogin() { return login; }
        public String getPassword() { return password; }
    }

    // Базовый метод регистрации через API
    private static User registerUser(String status) {
        String login = "user" + rnd.nextInt(10_000);
        String password = "pass" + rnd.nextInt(10_000);
        RegistrationDto dto = new RegistrationDto(login, password, status);

        given()
                .spec(requestSpec)
                .body(gson.toJson(dto))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        return new User(login, password);
    }

    /** Возвращает и регистрирует в системе активного пользователя */
    public static User getRegisteredActiveUser() {
        return registerUser("active");
    }

    /** Возвращает и регистрирует в системе заблокированного пользователя */
    public static User getRegisteredBlockedUser() {
        return registerUser("blocked");
    }

    /** Возвращает случайного незарегистрированного пользователя */
    public static User getUnregisteredUser() {
        String login = "user" + rnd.nextInt(10_000);
        String password = "pass" + rnd.nextInt(10_000);
        return new User(login, password);
    }
}
