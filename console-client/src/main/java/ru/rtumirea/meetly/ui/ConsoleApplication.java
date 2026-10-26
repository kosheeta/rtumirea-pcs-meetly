package ru.rtumirea.meetly.ui;

import ru.rtumirea.meetly.model.Booking;
import ru.rtumirea.meetly.model.BookingStatus;
import ru.rtumirea.meetly.model.Room;
import ru.rtumirea.meetly.model.User;
import ru.rtumirea.meetly.service.BookingService;
import ru.rtumirea.meetly.service.ExportService;
import ru.rtumirea.meetly.service.RoomService;
import ru.rtumirea.meetly.service.StatisticsService;
import ru.rtumirea.meetly.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsoleApplication {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final InputReader input = new InputReader();
    private final UserService userService = new UserService();
    private final RoomService roomService = new RoomService();
    private final BookingService bookingService = new BookingService();
    private final StatisticsService statisticsService = new StatisticsService();
    private final ExportService exportService = new ExportService();

    public void start() {

        menuLoop: while (true) {
            printMainMenu();

            int choice = input.readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> usersMenu();
                case 2 -> roomsMenu();
                case 3 -> bookingsMenu();
                case 4 -> searchMenu();
                case 5 -> filterMenu();
                case 6 -> sortMenu();
                case 7 -> showStatistics();
                case 8 -> exportData();
                case 0 -> {
                    break menuLoop;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n" +
                "======================================================\n" +
                "              СИСТЕМА БРОНИРОВАНИЯ                   \n" +
                "                  ПЕРЕГОВОРНЫХ                        \n" +
                "======================================================\n" +
                "1. Пользователи\n" +
                "2. Переговорные\n" +
                "3. Бронирования\n" +
                "4. Поиск\n" +
                "5. Фильтрация\n" +
                "6. Сортировка\n" +
                "7. Статистика\n" +
                "8. Экспорт данных\n" +
                "0. Выход\n" +
                "------------------------------------------------------");
    }

    private void usersMenu() {

        submenuLoop: while (true) {
            System.out.println("\n" +
                    "------------------ Пользователи ---------------------\n" +
                    "1. Список пользователей\n" +
                    "2. Добавить пользователя\n" +
                    "3. Удалить пользователя\n" +
                    "0. Назад\n" +
                    "------------------------------------------------------");

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
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void deleteUser() {

        int id = input.readInt("ID пользователя для удаления: ");

        try {
            userService.delete(id);
            System.out.println("Пользователь удалён.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void roomsMenu() {

        submenuLoop: while (true) {
            System.out.println("\n" +
                    "------------------ Переговорные ---------------------\n" +
                    "1. Список переговорных\n" +
                    "2. Добавить переговорную\n" +
                    "3. Удалить переговорную\n" +
                    "0. Назад\n" +
                    "------------------------------------------------------");

            int choice = input.readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> listRooms();
                case 2 -> createRoom();
                case 3 -> deleteRoom();
                case 0 -> {
                    break submenuLoop;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void listRooms() {
        showRooms(roomService.findAll());
    }

    private void printRooms(List<Room> rooms) {

        System.out.println("\nСписок переговорных:");
        System.out.printf("%-5s %-30s %-12s %-30s%n", "ID", "Название", "Вместимость", "Адрес");

        for (Room room : rooms) {
            System.out.printf("%-5d %-30s %-12d %-30s%n", room.getId(), room.getName(), room.getCapacity(), room.getAddress());
        }
    }

    private void createRoom() {

        String name = input.readString("Название: ");
        int capacity = input.readInt("Вместимость: ");
        String address = input.readString("Адрес: ");

        try {
            Room created = roomService.create(name, capacity, address);
            System.out.println("Переговорная создана, id: " + created.getId());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void deleteRoom() {

        int id = input.readInt("ID переговорной для удаления: ");

        try {
            roomService.delete(id);
            System.out.println("Переговорная удалена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void bookingsMenu() {

        submenuLoop: while (true) {
            System.out.println("\n" +
                    "------------------ Бронирования ---------------------\n" +
                    "1. Список бронирований\n" +
                    "2. Создать бронирование\n" +
                    "3. Отменить бронирование\n" +
                    "4. Удалить бронирование\n" +
                    "0. Назад\n" +
                    "------------------------------------------------------");

            int choice = input.readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> listBookings();
                case 2 -> createBooking();
                case 3 -> cancelBooking();
                case 4 -> deleteBooking();
                case 0 -> {
                    break submenuLoop;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void listBookings() {
        showBookings(bookingService.findAll());
    }

    private void printBookings(List<Booking> bookings) {

        System.out.println("\nСписок бронирований:");
        System.out.printf("%-5s %-10s %-10s %-20s %-20s %-12s%n",
                "ID", "User ID", "Room ID", "Начало", "Окончание", "Статус");

        for (Booking booking : bookings) {
            System.out.printf("%-5d %-10d %-10d %-20s %-20s %-12s%n",
                    booking.getId(),
                    booking.getUserId(),
                    booking.getRoomId(),
                    booking.getStartTime().format(DATE_TIME_FORMAT),
                    booking.getEndTime().format(DATE_TIME_FORMAT),
                    translateStatus(booking.getStatus()));
        }
    }

    private void createBooking() {

        int userId = input.readInt("ID пользователя: ");
        int roomId = input.readInt("ID переговорной: ");
        LocalDateTime startTime = input.readDateTime("Начало (дд.ММ.гггг ЧЧ:мм): ");
        LocalDateTime endTime = input.readDateTime("Окончание (дд.ММ.гггг ЧЧ:мм): ");

        try {
            Booking created = bookingService.create(userId, roomId, startTime, endTime);
            System.out.println("Бронирование создано, id: " + created.getId());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void cancelBooking() {

        int id = input.readInt("ID бронирования для отмены: ");

        try {
            bookingService.cancel(id);
            System.out.println("Бронирование отменено.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void deleteBooking() {

        int id = input.readInt("ID бронирования для удаления: ");

        try {
            bookingService.delete(id);
            System.out.println("Бронирование удалено.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void searchMenu() {

        submenuLoop: while (true) {
            System.out.println("\n" +
                    "---------------------- Поиск ------------------------\n" +
                    "1. Переговорные по адресу\n" +
                    "2. Бронирования пользователя\n" +
                    "0. Назад\n" +
                    "------------------------------------------------------");

            int choice = input.readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> searchRoomsByAddress();
                case 2 -> searchBookingsByUser();
                case 0 -> {
                    break submenuLoop;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void searchRoomsByAddress() {

        String address = input.readString("Адрес (или его часть): ");

        try {
            List<Room> rooms = roomService.findByAddress(address);

            if (rooms.isEmpty()) {
                System.out.println("\nПереговорные по такому адресу не найдены.");
                return;
            }

            printRooms(rooms);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void searchBookingsByUser() {

        int userId = input.readInt("ID пользователя: ");

        try {
            List<Booking> bookings = bookingService.findByUserId(userId);

            if (bookings.isEmpty()) {
                System.out.println("\nУ пользователя нет бронирований.");
                return;
            }

            printBookings(bookings);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void filterMenu() {

        submenuLoop: while (true) {
            System.out.println("\n" +
                    "-------------------- Фильтрация ---------------------\n" +
                    "1. Переговорные по минимальной вместимости\n" +
                    "2. Бронирования по статусу\n" +
                    "0. Назад\n" +
                    "------------------------------------------------------");

            int choice = input.readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> filterRoomsByMinCapacity();
                case 2 -> filterBookingsByStatus();
                case 0 -> {
                    break submenuLoop;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void filterRoomsByMinCapacity() {

        int minCapacity = input.readInt("Минимальная вместимость: ");

        try {
            List<Room> rooms = roomService.findByMinCapacity(minCapacity);

            if (rooms.isEmpty()) {
                System.out.println("\nПереговорные с такой вместимостью не найдены.");
                return;
            }

            printRooms(rooms);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void filterBookingsByStatus() {

        System.out.println("\n" +
                "1. Активно\n" +
                "2. Отменено\n" +
                "3. Архив");

        int choice = input.readInt("Выберите статус: ");

        BookingStatus status = switch (choice) {
            case 1 -> BookingStatus.ACTIVE;
            case 2 -> BookingStatus.CANCELLED;
            case 3 -> BookingStatus.ARCHIVED;
            default -> null;
        };

        if (status == null) {
            System.out.println("Неизвестный статус.");
            return;
        }

        List<Booking> bookings = bookingService.findByStatus(status);

        if (bookings.isEmpty()) {
            System.out.println("\nБронирований с таким статусом не найдено.");
            return;
        }

        printBookings(bookings);
    }

    private void sortMenu() {

        submenuLoop: while (true) {
            System.out.println("\n" +
                    "------------------- Сортировка -----------------------\n" +
                    "1. Переговорные по вместимости\n" +
                    "2. Переговорные по названию\n" +
                    "3. Бронирования по времени начала\n" +
                    "4. Бронирования по статусу\n" +
                    "0. Назад\n" +
                    "------------------------------------------------------");

            int choice = input.readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> showRooms(roomService.findAllSortedByCapacity());
                case 2 -> showRooms(roomService.findAllSortedByName());
                case 3 -> showBookings(bookingService.findAllSortedByStartTime());
                case 4 -> showBookings(bookingService.findAllSortedByStatus());
                case 0 -> {
                    break submenuLoop;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void showRooms(List<Room> rooms) {

        if (rooms.isEmpty()) {
            System.out.println("\nПереговорных пока нет.");
            return;
        }

        printRooms(rooms);
    }

    private void showBookings(List<Booking> bookings) {

        if (bookings.isEmpty()) {
            System.out.println("\nБронирований пока нет.");
            return;
        }

        printBookings(bookings);
    }

    private void showStatistics() {

        StatisticsService.Statistics stats = statisticsService.collect();

        System.out.println("\n-------------------- Статистика ---------------------\n" +
                "Всего пользователей: " + stats.totalUsers() + "\n" +
                "Всего переговорных: " + stats.totalRooms() + "\n" +
                "Всего бронирований: " + stats.totalBookings() + "\n" +
                "Активных: " + stats.activeBookings() + "\n" +
                "Отменённых: " + stats.cancelledBookings() + "\n" +
                "Архивных: " + stats.archivedBookings() + "\n" +
                String.format("Средняя вместимость переговорной: %.1f%n", stats.averageRoomCapacity()) +
                "Вместимость самой большой переговорной: " + stats.largestRoomCapacity() + "\n" +
                "------------------------------------------------------");
    }

    private void exportData() {

        String path = input.readString("Путь к файлу для экспорта (например, export.xlsx): ");

        if (path.isBlank()) {
            path = "export.xlsx";
        }

        try {
            exportService.exportToExcel(path);
            System.out.println("Данные экспортированы в файл: " + path);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private String translateStatus(BookingStatus status) {
        return switch (status) {
            case ACTIVE -> "активно";
            case CANCELLED -> "отменено";
            case ARCHIVED -> "архив";
        };
    }
}
