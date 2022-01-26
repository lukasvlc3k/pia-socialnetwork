package com.vlc3k.piasocialnetwork.dto.response.chat;

import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.entities.ChatMessage;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.entities.UserBlock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private long timestamp;
    private UserDto userFrom;
    private UserDto userTo;
    private String message;

    public ChatMessageDto(ChatMessage chatMessage) {
        this.timestamp = chatMessage.getTimestamp();
        this.userFrom = new UserDto(chatMessage.getUserFrom());
        this.userTo = new UserDto(chatMessage.getUserTo());
        this.message = chatMessage.getMessage();
    }
}