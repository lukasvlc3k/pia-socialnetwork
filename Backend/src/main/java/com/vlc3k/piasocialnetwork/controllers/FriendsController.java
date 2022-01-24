package com.vlc3k.piasocialnetwork.controllers;


import com.vlc3k.piasocialnetwork.dto.request.friends.FriendRequestNew;
import com.vlc3k.piasocialnetwork.dto.request.friends.FriendRequestResolve;
import com.vlc3k.piasocialnetwork.dto.response.Result;
import com.vlc3k.piasocialnetwork.dto.response.friends.FriendRequestDto;
import com.vlc3k.piasocialnetwork.dto.response.friends.UserBlockDto;
import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.services.FriendsService;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendsController {
    private final FriendsService friendsService;
    private final UserService userService;


    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getMyFriends() {
        var currentUser = userService.getLoggedUserUpdated();

        if (currentUser.isEmpty()) {
            // current user not found
            return ResponseEntity.internalServerError().body(List.of());
        }

        var myFriends = currentUser.get().getFriends();

        var myFriendsDto = myFriends.stream().map(UserDto::new).toList();
        return ResponseEntity.ok(myFriendsDto);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDto>> getMyRequests() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var myRequests = friendsService.getFriendRequests(currentUser);
        var requestsDto = myRequests.stream().map(FriendRequestDto::new).toList();

        return ResponseEntity.ok(requestsDto);
    }

    @GetMapping("/blocks")
    public ResponseEntity<List<UserBlockDto>> getMyBlockedUsers() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var myBlocks = friendsService.getBlockedUsers(currentUser);
        var myBlocksDto = myBlocks.stream().map(UserBlockDto::new).toList();

        return ResponseEntity.ok(myBlocksDto);
    }

    @DeleteMapping("/blocks/{id}")
    public ResponseEntity<Result<Boolean>> unblock(@PathVariable(value = "id") String idS) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id;
        try {
            id = Long.parseLong(idS);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Result.err("Invalid ID format"));
        }

        var block = friendsService.getUserBlockById(id);
        if (block.isEmpty()) {
            return ResponseEntity.badRequest().body(Result.err("Block not found"));
        }

        if (block.get().getBlockedBy().getId() != currentUser.getId()) {
            return ResponseEntity.badRequest().body(Result.err("Not blocked by you"));
        }

        friendsService.unBlockUser(block.get());

        return ResponseEntity.ok(Result.ok("ok"));
    }

    @PostMapping("/requests")
    public ResponseEntity<Result<FriendRequestDto>> createNewFriendRequest(@Valid @RequestBody FriendRequestNew friendCreateRequest) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var userTo = userService.getById(friendCreateRequest.getUserId());

        if (userTo.isEmpty()) {
            return ResponseEntity.badRequest().body(Result.err("User not found"));
        }

        var friendRequestAlreadyExist = friendsService.existFriendRequest(currentUser, userTo.get());
        if (friendRequestAlreadyExist) {
            return ResponseEntity.badRequest().body(Result.err("Friend request already exist"));
        }

        var alreadyFriends = friendsService.existFriendship(currentUser, userTo.get());
        if (alreadyFriends) {
            return ResponseEntity.badRequest().body(Result.err("Users are already friends"));
        }

        var friendRequest = friendsService.sendFriendRequest(userTo.get());
        return ResponseEntity.ok(Result.ok(new FriendRequestDto(friendRequest)));
    }

    @PostMapping("/requests/{id}")
    public ResponseEntity<Result<FriendRequestDto>> resolveFriendRequest(@PathVariable(value = "id") String idS, @Valid @RequestBody FriendRequestResolve friendRequestResolve) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        long id;
        try {
            id = Long.parseLong(idS);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Result.err("Invalid ID format"));
        }

        var friendRequest = friendsService.getFriendRequestById(id);
        if (friendRequest.isEmpty()) {
            return ResponseEntity.badRequest().body(Result.err("Friend request not found"));
        }

        if (friendRequest.get().getUserTo().getId() != currentUser.getId()) {
            return ResponseEntity.badRequest().body(Result.err("Friend request not for current user"));
        }
        try {
            switch (friendRequestResolve.getResolveType()) {
                case ACCEPT -> {

                    friendsService.acceptFriendRequest(friendRequest.get());

                }
                case REJECT -> friendsService.rejectFriendRequest(friendRequest.get());
                case BLOCK -> friendsService.blockUser(friendRequest.get().getUserFrom());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Result.err("Unknown Error has occurred"));
        }

        return ResponseEntity.ok(Result.ok("ok"));
    }
}
