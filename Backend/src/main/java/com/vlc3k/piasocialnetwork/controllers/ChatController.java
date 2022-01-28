package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.response.chat.ChatMessageDto;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.services.ChatMessageService;
import com.vlc3k.piasocialnetwork.services.UserService;
import com.vlc3k.piasocialnetwork.utils.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @GetMapping("/users/{userId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(
            @PathVariable(value="userId") Long userId,
            @RequestParam(name = "withUserId") Long withUserId,
            @RequestParam(required = false, defaultValue = "10", name = "count") Integer count) {

        var currentUser = utils.getCurrentUser();
        if (currentUser.getId() != userId){
            // not authorized
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(List.of());
        }

        var user = userService.getById(withUserId);
        if (user.isEmpty())
        {
            // user not found
            return ResponseEntity.badRequest().body(List.of());
        }


        var pageable = utils.getPageable(count);
        var messages = chatMessageService.GetMessages(currentUser, user.get(), pageable);

        var messagesDto = messages.stream().map(ChatMessageDto::new).toList();

        return ResponseEntity.ok(messagesDto);
    }
}
