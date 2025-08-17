package ru.netology.shop.data;

import ru.netology.shop.dto.RegistrationDto;

import java.util.Random;
import java.util.UUID;

public class DataHelper {
    private static final Random random = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

    private DataHelper() {}

    public static RegistrationDto getActiveUser() {
        return new RegistrationDto(
                generateRandomLogin(),
                generateRandomPassword(),
                "active"
        );
    }

    public static RegistrationDto getBlockedUser() {
        return new RegistrationDto(
                generateRandomLogin(),
                generateRandomPassword(),
                "blocked"
        );
    }

    public static String generateRandomLogin() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateRandomPassword() {
        int length = 10 + random.nextInt(7); // Длина 10-16 символов
        StringBuilder sb = new StringBuilder(length);

        sb.append((char) ('A' + random.nextInt(26)));

        sb.append((char) ('0' + random.nextInt(10)));

        sb.append("!@#$%^&*".charAt(random.nextInt(8)));

        for (int i = 3; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        // Перемешиваем символы
        char[] chars = sb.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int j = random.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }
}