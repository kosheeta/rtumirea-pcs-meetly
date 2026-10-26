package ru.rtumirea.meetly.service;

import ru.rtumirea.meetly.model.User;
import ru.rtumirea.meetly.model.UserRole;
import ru.rtumirea.meetly.repository.UserRepository;

public class AuthService {
    private final UserRepository users = new UserRepository();

    public User login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Введите email и пароль");
        }
        User user = users.findByCredentials(email.trim(), password);
        if (user == null) {
            throw new IllegalArgumentException("Неверный email или пароль");
        }
        if (user.getRole() != UserRole.SERVICE_ACCOUNT) {
            throw new IllegalArgumentException("Вход в админку доступен только сервисному аккаунту");
        }
        return user;
    }
}
