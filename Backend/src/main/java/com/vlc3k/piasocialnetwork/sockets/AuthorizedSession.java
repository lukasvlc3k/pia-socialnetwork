package com.vlc3k.piasocialnetwork.sockets;

import com.vlc3k.piasocialnetwork.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Data
public class AuthorizedSession {
    private WebSocketSession session;
    private User user;
    private UserDetails userDetails;
}