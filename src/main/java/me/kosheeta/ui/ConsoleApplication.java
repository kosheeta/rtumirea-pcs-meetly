package me.kosheeta.ui;

public class ConsoleApplication {
    private final InputReader input = new InputReader();

    public void start() {

        boolean running = true;

        while (running) {
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
                    running = false;
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
//        System.out.println("0. Выход");

        System.out.println("------------------------------------------------------");
    }

    private void usersMenu() {
        System.out.println("\nРаздел пользователей");
    }
}
