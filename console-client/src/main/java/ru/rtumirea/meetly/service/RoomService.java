package ru.rtumirea.meetly.service;

import ru.rtumirea.meetly.exception.EntityNotFoundException;
import ru.rtumirea.meetly.exception.ValidationException;
import ru.rtumirea.meetly.model.Room;
import ru.rtumirea.meetly.repository.RoomRepository;

import java.util.Comparator;
import java.util.List;

public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService() {
        this.roomRepository = new RoomRepository();
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room create(String name, int capacity, String address) {

        if (name == null || name.isBlank()) {
            throw new ValidationException("Название переговорной не может быть пустым.");
        }

        if (capacity <= 0) {
            throw new ValidationException("Вместимость должна быть положительным числом.");
        }

        if (address == null || address.isBlank()) {
            throw new ValidationException("Адрес не может быть пустым.");
        }

        Room room = new Room();
        room.setName(name.trim());
        room.setCapacity(capacity);
        room.setAddress(address.trim());

        return roomRepository.save(room);
    }

    public Room findById(int id) {
        return roomRepository.findById(id);
    }

    public List<Room> findByAddress(String address) {

        if (address == null || address.isBlank()) {
            throw new ValidationException("Адрес для поиска не может быть пустым.");
        }

        return roomRepository.findByAddress(address.trim());
    }

    public List<Room> findByMinCapacity(int minCapacity) {

        if (minCapacity <= 0) {
            throw new ValidationException("Вместимость должна быть положительным числом.");
        }

        return roomRepository.findByMinCapacity(minCapacity);
    }

    public List<Room> findAllSortedByCapacity() {
        return roomRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Room::getCapacity))
                .toList();
    }

    public List<Room> findAllSortedByName() {
        return roomRepository.findAll().stream()
                .sorted(Comparator.comparing(Room::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public void delete(int id) {

        Room existing = roomRepository.findById(id);

        if (existing == null) {
            throw new EntityNotFoundException("Переговорная с id " + id + " не найдена.");
        }

        roomRepository.deleteById(id);
    }
}
