package me.kosheeta.ui;

import java.util.Scanner;

public class InputReader {
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
}
