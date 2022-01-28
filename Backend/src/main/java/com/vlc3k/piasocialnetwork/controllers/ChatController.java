package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.response.Result;
import com.vlc3k.piasocialnetwork.dto.response.chat.ChatMessagesDto;
import com.vlc3k.piasocialnetwork.dto.response.post.PostDto;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.services.ChatMessageService;
import com.vlc3k.piasocialnetwork.services.UserService;
import com.vlc3k.piasocialnetwork.utils.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat-messages")
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<Result<ChatMessagesDto>> getChatMessages(
            @RequestParam(name = "withUserID") String withUserS,
            @RequestParam(required = false, defaultValue = "10", name = "count") String countS) {

        long userId;
        int count;
        try {
            userId = Long.parseLong(withUserS);
            count = Integer.parseInt(countS);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Result.err("Invalid number (id or count) format"));
        }

        var user = userService.getById(userId);
        if (user.isEmpty()) {
            // user not found
            return ResponseEntity.badRequest().body(Result.err("User not found"));
        }

        User currentUser = utils.getCurrentUser();

        var pageable = utils.getPageable(count);
        var messages = chatMessageService.GetMessages(currentUser, user.get(), pageable);

        return ResponseEntity.ok(Result.ok(new ChatMessagesDto(user.get(), messages)));
    }
}
