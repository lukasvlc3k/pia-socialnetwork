package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User getByEmail(String email);
    Optional<User> tryGetByEmail(String email);

    User getById(Long id);

    User registerUser(String email, String name,  String password, List<Role> roles);
    User registerUser(String email, String name,  String password, Set<Role> roles);

    List<User> getUsers();

    boolean existsEmail(String email);
}
