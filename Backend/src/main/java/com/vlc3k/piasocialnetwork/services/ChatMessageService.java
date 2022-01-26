package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.entities.ChatMessage;
import com.vlc3k.piasocialnetwork.entities.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatMessageService {
    public ChatMessage AddMessage(User userFrom, User userTo, String message);

    public List<ChatMessage> GetMessages(User user1, User user2, Pageable pageable);
}
