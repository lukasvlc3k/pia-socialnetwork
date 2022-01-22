package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.entities.User;

import java.util.List;

public interface UserService {
    User registerUser(String email, String password);

    List<User> getUsers();
}
