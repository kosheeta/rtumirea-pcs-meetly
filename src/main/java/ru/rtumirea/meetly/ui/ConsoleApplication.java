package ru.rtumirea.meetly.ui;

import ru.rtumirea.meetly.exception.MeetlyException;
import ru.rtumirea.meetly.model.Booking;
import ru.rtumirea.meetly.model.BookingStatus;
import ru.rtumirea.meetly.model.Room;
import ru.rtumirea.meetly.model.User;
import ru.rtumirea.meetly.service.BookingService;
import ru.rtumirea.meetly.service.RoomService;
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

    public void start() {

        menuLoop: while (true) {
            printMainMenu();

            int choice = input.readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> usersMenu();
                case 2 -> roomsMenu();
                case 3 -> bookingsMenu();
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
        System.out.println("2. Переговорные");
        System.out.println("3. Бронирования");
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
        } catch (MeetlyException e) {
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
        } catch (MeetlyException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Не удалось удалить пользователя. Возможно, есть связанные бронирования.");
        }
    }

    private void roomsMenu() {

        submenuLoop: while (true) {
            System.out.println();
            System.out.println("------------------ Переговорные ---------------------");
            System.out.println("1. Список переговорных");
            System.out.println("2. Добавить переговорную");
            System.out.println("3. Удалить переговорную");
            System.out.println("0. Назад");
            System.out.println("------------------------------------------------------");

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

        List<Room> rooms = roomService.findAll();

        if (rooms.isEmpty()) {
            System.out.println("\nПереговорных пока нет.");
            return;
        }

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
        } catch (MeetlyException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Не удалось создать переговорную.");
        }
    }

    private void deleteRoom() {

        int id = input.readInt("ID переговорной для удаления: ");

        try {
            roomService.delete(id);
            System.out.println("Переговорная удалена.");
        } catch (MeetlyException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Не удалось удалить переговорную. Возможно, есть связанные бронирования.");
        }
    }

    private void bookingsMenu() {

        submenuLoop: while (true) {
            System.out.println();
            System.out.println("------------------ Бронирования ---------------------");
            System.out.println("1. Список бронирований");
            System.out.println("2. Создать бронирование");
            System.out.println("3. Отменить бронирование");
            System.out.println("4. Удалить бронирование");
            System.out.println("0. Назад");
            System.out.println("------------------------------------------------------");

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

        List<Booking> bookings = bookingService.findAll();

        if (bookings.isEmpty()) {
            System.out.println("\nБронирований пока нет.");
            return;
        }

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
        } catch (MeetlyException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Не удалось создать бронирование.");
        }
    }

    private void cancelBooking() {

        int id = input.readInt("ID бронирования для отмены: ");

        try {
            bookingService.cancel(id);
            System.out.println("Бронирование отменено.");
        } catch (MeetlyException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Не удалось отменить бронирование.");
        }
    }

    private void deleteBooking() {

        int id = input.readInt("ID бронирования для удаления: ");

        try {
            bookingService.delete(id);
            System.out.println("Бронирование удалено.");
        } catch (MeetlyException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Не удалось удалить бронирование.");
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
