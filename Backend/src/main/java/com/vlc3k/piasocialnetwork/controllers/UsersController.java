package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.request.post.PostCreateRequest;
import com.vlc3k.piasocialnetwork.dto.request.user.SetAdminRequest;
import com.vlc3k.piasocialnetwork.dto.response.Result;
import com.vlc3k.piasocialnetwork.dto.response.post.PostDto;
import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.ECanBeAddedToFriendsType;
import com.vlc3k.piasocialnetwork.enums.EPostType;
import com.vlc3k.piasocialnetwork.enums.ERole;
import com.vlc3k.piasocialnetwork.services.RoleService;
import com.vlc3k.piasocialnetwork.services.UserService;
import com.vlc3k.piasocialnetwork.utils.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;
    private final RoleService roleService;

    /**
     * Returns the UserDto object for current user
     *
     * @return
     */
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


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/admin")
    public ResponseEntity<Result<String>> setAdmin(@PathVariable(value = "id") String idS, @Valid @RequestBody SetAdminRequest setAdminRequest) {
        User currentUser = utils.getCurrentUser();

        long id;
        try {
            id = Long.parseLong(idS);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Result.err("Invalid ID format"));
        }

        var user = userService.getById(id);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(Result.err("User not found"));
        }

        if (user.get().getId() == currentUser.getId()) {
            return ResponseEntity.badRequest().body(Result.err("Can not modify yourselves"));
        }

        var adminRole = roleService.getByName(ERole.ROLE_ADMIN);
        if (adminRole.isEmpty()) {
            return ResponseEntity.internalServerError().body(Result.err("Admin role not found"));
        }

        if (setAdminRequest.isAddAdmin()) {
            userService.addRole(user.get(), adminRole.get());
        } else {
            userService.removeRole(user.get(), adminRole.get());
        }

        return ResponseEntity.ok(Result.ok(""));
    }
}
