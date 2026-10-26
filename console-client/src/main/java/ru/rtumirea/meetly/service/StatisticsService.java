package ru.rtumirea.meetly.service;

import ru.rtumirea.meetly.model.Booking;
import ru.rtumirea.meetly.model.BookingStatus;
import ru.rtumirea.meetly.model.Room;

import java.util.List;

public class StatisticsService {
    private final UserService userService;
    private final RoomService roomService;
    private final BookingService bookingService;

    public StatisticsService() {
        this.userService = new UserService();
        this.roomService = new RoomService();
        this.bookingService = new BookingService();
    }

    public Statistics collect() {

        List<Room> rooms = roomService.findAll();
        List<Booking> bookings = bookingService.findAll();

        long activeBookings = bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.ACTIVE)
                .count();

        long cancelledBookings = bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CANCELLED)
                .count();

        long archivedBookings = bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.ARCHIVED)
                .count();

        double averageRoomCapacity = rooms.stream()
                .mapToInt(Room::getCapacity)
                .average()
                .orElse(0);

        int largestRoomCapacity = rooms.stream()
                .mapToInt(Room::getCapacity)
                .max()
                .orElse(0);

        return new Statistics(
                userService.findAll().size(),
                rooms.size(),
                bookings.size(),
                activeBookings,
                cancelledBookings,
                archivedBookings,
                averageRoomCapacity,
                largestRoomCapacity
        );
    }

    public record Statistics(
            int totalUsers,
            int totalRooms,
            int totalBookings,
            long activeBookings,
            long cancelledBookings,
            long archivedBookings,
            double averageRoomCapacity,
            int largestRoomCapacity
    ) {
    }
}
