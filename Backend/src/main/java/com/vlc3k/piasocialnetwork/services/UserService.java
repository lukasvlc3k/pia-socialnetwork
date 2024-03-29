package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.ERole;
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

    void changeIsOnline(User user, boolean isOnline);

    List<User> findUsersInRole(ERole role);

    List<User> getByOnline(boolean online);

    void addRole(User user, Role role);
    void removeRole(User user, Role role);
}
