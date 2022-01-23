package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.repositories.UserRepository;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("userService")
@Getter
@Setter
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> tryGetByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public User getByEmail(String email) {
        var user = tryGetByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException("Email not found");
        }

        return user.get();
    }

    @Override
    public User getById(Long id) {
        var user = repo.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User ID not found");
        }

        return user.get();
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

        usr.setRoles(new HashSet<>(roles));

        return repo.save(usr);
    }

    public List<User> getUsers() {
        return repo.findAll();
    }

    @Override
    public boolean existsEmail(String email) {
        return repo.existsByEmail(email);
    }
}
