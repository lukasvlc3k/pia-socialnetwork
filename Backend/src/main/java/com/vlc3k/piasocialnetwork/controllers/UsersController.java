package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.ECanBeAddedToFriendsType;
import com.vlc3k.piasocialnetwork.services.UserService;
import com.vlc3k.piasocialnetwork.utils.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        var currentUser = userService.getLoggedUserUpdated().orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        return ResponseEntity.ok(new UserDto(currentUser));
    }

    @GetMapping("/results/{search}")
    public ResponseEntity<List<UserDto>> searchRelevantUsers(@PathVariable(value = "search") String search) {
        if (search.length() < 3) {
            // at least 3 chars must be provided
            return ResponseEntity.badRequest().body(List.of());
        }

        User currentUser = userService.getLoggedUserUpdated().get();
        //User currentUser = userService.getById(loggedUser.getId()).get();

        var pageable = utils.getPageable(-1);
        var users = userService.getRelevantUsers(search, pageable);

        var blockedIds = currentUser.getBlockedUsers().stream().map(u -> u.getBlockedUser().getId()).toList();
        var friendsIds = currentUser.getFriends().stream().map(User::getId).toList();
        var friendRequestsSentTo = currentUser.getSentFriendRequests().stream().map(fr -> fr.getUserTo().getId()).toList();
        var friendRequestReceivedFrom = currentUser.getReceivedFriendRequests().stream().map(fr -> fr.getUserFrom().getId()).toList();

        var usersDto = users.stream().map(UserDto::new).toList();
        for (var usr : usersDto) {
            if (blockedIds.contains(usr.getId())) {
                usr.setCanBeAddedToFriendsType(ECanBeAddedToFriendsType.NO_BLOCKED);
            } else if (friendsIds.contains(usr.getId())) {
                usr.setCanBeAddedToFriendsType(ECanBeAddedToFriendsType.NO_ALREADY_FRIEND);
            } else if (friendRequestsSentTo.contains(usr.getId())) {
                usr.setCanBeAddedToFriendsType(ECanBeAddedToFriendsType.NO_FRIEND_REQUEST_SENT);
            } else if (friendRequestReceivedFrom.contains(usr.getId())) {
                usr.setCanBeAddedToFriendsType(ECanBeAddedToFriendsType.NO_FRIEND_REQUEST_PENDING);
            } else if (usr.getId() == currentUser.getId()) {
                usr.setCanBeAddedToFriendsType(ECanBeAddedToFriendsType.NO_ME);
            } else {
                usr.setCanBeAddedToFriendsType(ECanBeAddedToFriendsType.YES);
            }
        }

        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }
}
