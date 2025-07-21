package ru.netology.shop.data;

import ru.netology.shop.dto.RegistrationDto;

public class DataHelper {
    private DataHelper() {}

    public static RegistrationDto getActiveUser() {
        return new RegistrationDto("vasya", "qwerty123", "active");
    }

    public static RegistrationDto getBlockedUser() {
        return new RegistrationDto("petya", "qwerty123", "blocked");
    }

    public static String getInvalidLogin() {
        return "invalid_login";
    }

    public static String getInvalidPassword() {
        return "wrong_password";
    }
}