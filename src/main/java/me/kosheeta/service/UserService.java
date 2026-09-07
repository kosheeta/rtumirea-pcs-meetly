package me.kosheeta.service;

import me.kosheeta.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }
}
