package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static final Faker faker = new Faker();
    private static final Gson gson = new Gson();

    private static final RequestSpecification requestSpec = RestAssured
            .given()
            .baseUri("http://localhost")
            .port(9999)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .log().all();

    public static class User {
        public final String login;
        public final String password;
        public final String status;

        public User(String login, String password, String status) {
            this.login = login;
            this.password = password;
            this.status = status;
        }
    }

    public static User createUser(String status) {
        User user = new User(faker.name().username(), faker.internet().password(), status);
        given()
                .spec(requestSpec)
                .body(gson.toJson(user))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
        return user;
    }

    public static User getActiveUser() {
        return createUser("active");
    }

    public static User getBlockedUser() {
        return createUser("blocked");
    }

    public static User getUserWithWrongLogin() {
        User user = getActiveUser();
        return new User("wrong_" + user.login, user.password, "active");
    }

    public static User getUserWithWrongPassword() {
        User user = getActiveUser();
        return new User(user.login, "wrong_password", "active");
    }
}
