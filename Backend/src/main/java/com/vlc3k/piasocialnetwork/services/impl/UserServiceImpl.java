package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.repositories.UserRepository;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Service("userService")
@Getter
@Setter
@RequiredArgsConstructor
@RequestScope
public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    @Override
    public User registerUser(String email, String password) {
        var usr = new User();
        usr.setEmail(email);
        usr.setPassword(password);
        usr.setName("test");
        return repo.save(usr);
    }

    public List<User> getUsers() {
        return repo.findAll();
    }
}
