package com.vlc3k.piasocialnetwork.sockets;

import com.vlc3k.piasocialnetwork.entities.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocketChatMessage {
    private long userFromId;
    private long userToId;
    private long timestamp;
    private String message;

    public SocketChatMessage(ChatMessage chatMessage) {
        this.userFromId = chatMessage.getUserFrom().getId();
        this.userToId = chatMessage.getUserTo().getId();
        this.timestamp = chatMessage.getTimestamp();
        this.message = chatMessage.getMessage();
    }
}
