package org.example.youth_be.user.enums;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum UserRoleEnum{
    ASSOCIATE("준회원"),
    REGULAR("정회원");

    private final String description;

    UserRoleEnum(String description) {
        this.description = description;
    }

    public static UserRoleEnum fromValue(String value) {
        return Arrays.stream(UserRoleEnum.values()).filter(role -> role.name().equals(value)).findFirst().get();
    }

    public String getDescription() {
        return description;
    }
}
