package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.ERole;
import com.vlc3k.piasocialnetwork.repositories.UserRepository;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("userService")
@Getter
@Setter
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User registerUser(String email, String name, String password, List<Role> roles) {
        return this.registerUser(email, name, password, new HashSet<>(roles));
    }

    @Override
    public User registerUser(String email, String name, String password, Set<Role> roles) {
        var usr = new User();
        usr.setEmail(email);
        usr.setPassword(passwordEncoder.encode(password));
        usr.setName(name);
        usr.setOnline(false);

        usr.setRoles(new HashSet<>(roles));

        return userRepository.save(usr);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public List<User> getRelevantUsers(String searchFor, Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findRelevantUsers(currentUser.getId(), searchFor, pageable);
    }

    @Override
    public Optional<User> getLoggedUserUpdated() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getById(currentUser.getId());
    }

    @Override
    public void changeIsOnline(User user, boolean isOnline) {
        user.setOnline(isOnline);
        userRepository.save(user);
    }

    @Override
    public List<User> findUsersInRole(ERole role) {
        return userRepository.findUsersInRole(role);
    }

    @Override
    public void addRole(User user, Role role) {
        if (user.getRoles().contains(role)) {
            // user already has this role
            return;
        }

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public void removeRole(User user, Role role) {
        if (!user.getRoles().contains(role)) {
            // user does not have this role
            return;
        }

        user.getRoles().remove(role);
        userRepository.save(user);
    }
}
