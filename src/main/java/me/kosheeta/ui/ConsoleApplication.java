package me.kosheeta.ui;

import me.kosheeta.model.User;
import me.kosheeta.service.UserService;

import java.util.List;

public class ConsoleApplication {
    private final InputReader input = new InputReader();
    private final UserService userService = new UserService();

    public void start() {

        menuLoop: while (true) {
            printMainMenu();

            int choice = input.readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> usersMenu();
//                case 2 -> roomsMenu();
//                case 3 -> bookingsMenu();
//                case 4 -> searchMenu();
//                case 5 -> statisticsMenu();
//                case 6 -> exportMenu();
//                case 7 -> databaseMenu();
                case 0 -> {
                    System.out.println("До свидания!");
                    break menuLoop;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void printMainMenu() {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("              СИСТЕМА БРОНИРОВАНИЯ                   ");
        System.out.println("                  ПЕРЕГОВОРНЫХ                        ");
        System.out.println("======================================================");

        System.out.println("1. Пользователи");
//        System.out.println("2. Переговорные");
//        System.out.println("3. Бронирования");
//        System.out.println("4. Поиск");
//        System.out.println("5. Фильтрация");
//        System.out.println("6. Статистика");
//        System.out.println("7. Экспорт данных");
//        System.out.println("8. Вывести таблицы базы данных");
        System.out.println("0. Выход");

        System.out.println("------------------------------------------------------");
    }

    private void usersMenu() {

        submenuLoop: while (true) {
            System.out.println();
            System.out.println("------------------ Пользователи ---------------------");
            System.out.println("1. Список пользователей");
            System.out.println("2. Добавить пользователя");
            System.out.println("3. Удалить пользователя");
            System.out.println("0. Назад");
            System.out.println("------------------------------------------------------");

            int choice = input.readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> listUsers();
                case 2 -> createUser();
                case 3 -> deleteUser();
                case 0 -> {
                    break submenuLoop;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void listUsers() {

        List<User> users = userService.findAll();

        if (users.isEmpty()) {
            System.out.println("\nПользователей пока нет.");
            return;
        }

        System.out.println("\nСписок пользователей:");
        System.out.printf("%-5s %-30s %-30s%n", "ID", "Имя", "Email");

        for (User user : users) {
            System.out.printf("%-5d %-30s %-30s%n", user.getId(), user.getName(), user.getEmail());
        }
    }

    private void createUser() {

        String name = input.readString("Имя: ");
        String email = input.readString("Email: ");

        try {
            User created = userService.create(name, email);
            System.out.println("Пользователь создан, id: " + created.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Не удалось создать пользователя. Возможно, такой email уже используется.");
        }
    }

    private void deleteUser() {

        int id = input.readInt("ID пользователя для удаления: ");

        try {
            userService.delete(id);
            System.out.println("Пользователь удалён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Не удалось удалить пользователя. Возможно, есть связанные бронирования.");
        }
    }
}
