package ru.rtumirea.meetly.service;

import ru.rtumirea.meetly.exception.EntityNotFoundException;
import ru.rtumirea.meetly.exception.ValidationException;
import ru.rtumirea.meetly.model.User;
import ru.rtumirea.meetly.repository.UserRepository;

import java.util.List;
import java.util.regex.Pattern;

public class UserService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id);
    }

    public User create(String name, String email) {

        if (name == null || name.isBlank()) {
            throw new ValidationException("Имя не может быть пустым.");
        }

        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Некорректный формат email.");
        }

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim());

        return userRepository.save(user);
    }

    public void delete(int id) {

        User existing = userRepository.findById(id);

        if (existing == null) {
            throw new EntityNotFoundException("Пользователь с id " + id + " не найден.");
        }

        userRepository.deleteById(id);
    }
}
