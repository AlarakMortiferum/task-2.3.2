package ru.netology.testmode.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

import java.util.Random;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();

    private static final Random random = new Random();

    // Класс пользователя
    public static class User {
        private String login;
        private String password;
        private String status;

        public User(String login, String password, String status) {
            this.login = login;
            this.password = password;
            this.status = status;
        }

        public String getLogin() {
            return login;
        }
        public String getPassword() {
            return password;
        }
        public String getStatus() {
            return status;
        }
    }

    // Метод регистрации пользователя через API
    public static void registerUser(User user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static User getActiveUser() {
        User user = new User(generateRandomLogin(), generateRandomPassword(), "active");
        registerUser(user);
        return user;
    }

    public static User getBlockedUser() {
        User user = new User(generateRandomLogin(), generateRandomPassword(), "blocked");
        registerUser(user);
        return user;
    }

    public static User getUserWithWrongLogin() {
        // Возвращаем пользователя с несуществующим логином (не регистрируем)
        return new User("wrongLogin" + generateRandomLogin(), "somePassword123", "active");
    }

    public static User getUserWithWrongPassword() {
        User user = new User(generateRandomLogin(), generateRandomPassword(), "active");
        registerUser(user);
        return new User(user.getLogin(), "wrongPassword123", "active");
    }

    private static String generateRandomLogin() {
        return "user" + random.nextInt(10000);
    }

    private static String generateRandomPassword() {
        return "pass" + random.nextInt(10000);
    }

    public static class Verification {
        private String login;
        private String code;

        public Verification(String login, String code) {
            this.login = login;
            this.code = code;
        }

        public String getLogin() {
            return login;
        }

        public String getCode() {
            return code;
        }
    }
}
