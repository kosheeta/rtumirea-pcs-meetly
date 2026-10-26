package ru.rtumirea.meetly.model;

import ru.rtumirea.meetly.exception.ValidationException;

public enum BookingStatus {
    ACTIVE("active"),
    CANCELLED("cancelled"),
    ARCHIVED("archived");

    private final String dbValue;

    BookingStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static BookingStatus fromDbValue(String dbValue) {
        for (BookingStatus status : values()) {
            if (status.dbValue.equals(dbValue)) {
                return status;
            }
        }
        throw new ValidationException("Неизвестный статус бронирования: " + dbValue);
    }
}
