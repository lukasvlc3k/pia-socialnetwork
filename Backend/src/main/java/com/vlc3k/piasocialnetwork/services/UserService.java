package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.entities.User;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    @Transactional
    Optional<User> getByEmail(String email);
    Optional<User> getById(Long id);

    User registerUser(String email, String name,  String password, List<Role> roles);
    User registerUser(String email, String name,  String password, Set<Role> roles);

    List<User> getUsers();

    boolean existsEmail(String email);

    List<User> getRelevantUsers(String searchFor, Pageable pageable);

    Optional<User> getLoggedUserUpdated();
}
