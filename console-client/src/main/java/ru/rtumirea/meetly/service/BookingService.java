package ru.rtumirea.meetly.service;

import ru.rtumirea.meetly.exception.EntityNotFoundException;
import ru.rtumirea.meetly.exception.ValidationException;
import ru.rtumirea.meetly.model.Booking;
import ru.rtumirea.meetly.model.BookingStatus;
import ru.rtumirea.meetly.model.Room;
import ru.rtumirea.meetly.model.User;
import ru.rtumirea.meetly.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;

    public BookingService() {
        this.bookingRepository = new BookingRepository();
        this.userService = new UserService();
        this.roomService = new RoomService();
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking create(int userId, int roomId, LocalDateTime startTime, LocalDateTime endTime) {

        User user = userService.findById(userId);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с id " + userId + " не найден.");
        }

        Room room = roomService.findById(roomId);
        if (room == null) {
            throw new EntityNotFoundException("Переговорная с id " + roomId + " не найдена.");
        }

        if (startTime == null || endTime == null) {
            throw new ValidationException("Время начала и окончания должно быть указано.");
        }

        if (!endTime.isAfter(startTime)) {
            throw new ValidationException("Время окончания должно быть позже времени начала.");
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Нельзя создать бронирование в прошлом.");
        }

        if (bookingRepository.existsOverlapping(roomId, startTime, endTime)) {
            throw new ValidationException("Переговорная уже забронирована на выбранное время.");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.ACTIVE);

        return bookingRepository.save(booking);
    }

    public List<Booking> findByUserId(int userId) {

        User user = userService.findById(userId);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с id " + userId + " не найден.");
        }

        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> findByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    public List<Booking> findAllSortedByStartTime() {
        return bookingRepository.findAll().stream()
                .sorted(Comparator.comparing(Booking::getStartTime))
                .toList();
    }

    public List<Booking> findAllSortedByStatus() {
        return bookingRepository.findAll().stream()
                .sorted(Comparator.comparing(Booking::getStatus))
                .toList();
    }

    public void cancel(int id) {

        Booking existing = bookingRepository.findById(id);

        if (existing == null) {
            throw new EntityNotFoundException("Бронирование с id " + id + " не найдено.");
        }

        if (existing.getStatus() != BookingStatus.ACTIVE) {
            throw new ValidationException("Отменить можно только активное бронирование.");
        }

        bookingRepository.updateStatus(id, BookingStatus.CANCELLED);
    }

    public void delete(int id) {

        Booking existing = bookingRepository.findById(id);

        if (existing == null) {
            throw new EntityNotFoundException("Бронирование с id " + id + " не найдено.");
        }

        bookingRepository.deleteById(id);
    }
}
