package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.entities.ChatMessage;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.repositories.ChatMessageRepository;
import com.vlc3k.piasocialnetwork.services.ChatMessageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service("chatMessageService")
@Getter
@Setter
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage AddMessage(User userFrom, User userTo, String message) {
        var msg = ChatMessage.builder()
                .userFrom(userFrom)
                .userTo(userTo)
                .message(message)
                .timestamp(new Date().getTime())
                .build();

        return chatMessageRepository.save(msg);
    }

    @Override
    public List<ChatMessage> GetMessages(User user1, User user2, Pageable pageable) {
        return chatMessageRepository.findChatMessagesBetweenUsers(user1, user2, pageable);
    }
}