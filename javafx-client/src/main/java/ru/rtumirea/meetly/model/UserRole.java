package ru.rtumirea.meetly.model;

import java.security.InvalidParameterException;

public enum UserRole {
    USER("user"),
    SERVICE_ACCOUNT("service_account");

    private final String dbValue;

    UserRole(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static UserRole fromDbValue(String dbValue) {
        for (UserRole role : values()) {
            if (role.dbValue.equals(dbValue)) {
                return role;
            }
        }
        throw new InvalidParameterException("Неизвестная роль пользователя: " + dbValue);
    }
}
