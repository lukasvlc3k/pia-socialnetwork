package com.vlc3k.piasocialnetwork.dto.response.chat;

import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.entities.ChatMessage;
import com.vlc3k.piasocialnetwork.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessagesDto {
    private UserDto chatWith;
    private List<ChatMessageDto> messages;

    public ChatMessagesDto(User user, List<ChatMessage> messages){
        this.chatWith = new UserDto(user);
        this.messages = messages.stream().map(ChatMessageDto::new).toList();
    }
}
