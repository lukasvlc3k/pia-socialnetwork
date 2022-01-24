package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.services.UserService;
import com.vlc3k.piasocialnetwork.utils.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @GetMapping("/results/{search}")
    public ResponseEntity<List<UserDto>> searchRelevantUsers(@PathVariable(value = "search") String search) {
        if (search.length() < 3) {
            // at least 3 chars must be provided
            return ResponseEntity.badRequest().body(List.of());
        }

        var pageable = utils.getPageable(-1);
        var users = userService.getRelevantUsers(search, pageable);

        var usersDto = users.stream().map(UserDto::new).toList();
        return new ResponseEntity<List<UserDto>>(usersDto, HttpStatus.OK);
    }
}
