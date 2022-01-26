package com.vlc3k.piasocialnetwork.sockets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocketMessage {
    private SocketMessageType type;
    private Object content;
}