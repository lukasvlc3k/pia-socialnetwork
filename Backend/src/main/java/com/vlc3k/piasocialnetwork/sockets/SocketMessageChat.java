package com.vlc3k.piasocialnetwork.sockets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocketMessageChat {
    private SocketMessageType type;
    private SocketChatMessage content;
}