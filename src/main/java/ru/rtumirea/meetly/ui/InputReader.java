package ru.rtumirea.meetly.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputReader {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final Scanner scanner = new Scanner(System.in);

    public int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    public String readString(String message) {

        System.out.print(message);
        return scanner.nextLine();
    }

    public long readLong(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Long.parseLong(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число.");
            }
        }
    }

    public LocalDateTime readDateTime(String message) {

        while (true) {

            System.out.print(message);

            try {
                return LocalDateTime.parse(scanner.nextLine().trim(), DATE_TIME_FORMAT);

            } catch (DateTimeParseException e) {
                System.out.println("Ошибка: введите дату и время в формате дд.ММ.гггг ЧЧ:мм.");
            }
        }
    }
}
