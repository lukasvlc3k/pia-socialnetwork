package com.vlc3k.piasocialnetwork.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.services.JwtAuthService;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger LOG = LoggerFactory.getLogger(SocketHandler.class);

    private final HashMap<String, AuthorizedSession> authorizedSessions = new HashMap<>();

    private final JwtAuthService jwtAuthService;
    private final UserService userService;


    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        LOG.error("error occured at sender " + session, throwable);

        if (session.isOpen()) {
            session.close(CloseStatus.PROTOCOL_ERROR);
        }
        afterConnectionClosed(session, CloseStatus.PROTOCOL_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.info(String.format("Session %s closed because of %s", session.getId(), status.getReason()));

        var authorizedSession = authorizedSessions.getOrDefault(session.getId(), null);
        if (authorizedSession != null) {
            onUserOnlineChange(authorizedSession.getUser(), false);
        }

        authorizedSessions.remove(session.getId());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("Connected " + session.getId() + " from ip " + session.getRemoteAddress().toString());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        LOG.info("Pong " + session.getId() + ", message " + message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        val socketMessage = objectMapper.readValue(jsonTextMessage.getPayload(), SocketMessage.class);
        LOG.info("message received: " + socketMessage);
        if (socketMessage == null) {
            return;
        }

        if (socketMessage.getType() == SocketMessageType.AUTHORIZATION) {
            if (socketMessage.getContent() == null) {
                session.close(CloseStatus.BAD_DATA);
                return;
            }

            val token = socketMessage.getContent().toString();
            var authRes = jwtAuthService.authUserByToken(token);

            if (authRes.isEmpty()) {
                sendMessage(session,
                        SocketMessage.builder()
                                .type(SocketMessageType.AUTHORIZATION)
                                .content("FAIL")
                                .build()
                );
                session.close();
                return;
            }

            authorizedSessions.put(session.getId(), new AuthorizedSession(session, authRes.get().getUser(), authRes.get().getUserDetails()));
            sendMessage(session,
                    SocketMessage.builder()
                            .type(SocketMessageType.AUTHORIZATION)
                            .content("SUCCESS")
                            .build()
            );
        }

        var authorizedSession = authorizedSessions.getOrDefault(session.getId(), null);
        if (authorizedSession == null) {
            LOG.info("Not Authorized: " + session.getId());
            sendMessage(session,
                    SocketMessage.builder()
                            .type(SocketMessageType.AUTHORIZATION)
                            .content("FAIL")
                            .build()
            );
            return;
        }

        switch (socketMessage.getType()) {
            case AUTHORIZATION -> {
                onUserOnlineChange(authorizedSession.getUser(), true);
            }
            case CHAT -> {
                var chatMessage = objectMapper.readValue(socketMessage.getContent().toString(), ChatMessage.class);
                sendMessage(session, new SocketMessage(SocketMessageType.CHAT, "ok"));
            }
        }
    }

    private void onUserOnlineChange(User user, boolean isOnline) {
        userService.changeIsOnline(user, isOnline);
        LOG.info(user.getId() + " is " + isOnline);

        var activeSessions = authorizedSessions.entrySet().stream().filter(
                        (kv) -> kv.getValue().getSession().isOpen() && kv.getValue().getUser().getId() == user.getId())
                .count();
        if (!isOnline && activeSessions > 0) {
            return;
        }

        authorizedSessions.forEach((key, value) -> {
            try {
                var session = value.getSession();
                if (session.isOpen()) {
                    sendMessage(session, new SocketMessage(isOnline ? SocketMessageType.USER_JOIN : SocketMessageType.USER_LEAVE, user.getId()));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void sendMessage(WebSocketSession webSocketSession, SocketMessage socketMessage) throws IOException {
        webSocketSession.sendMessage(new TextMessage(serialize(socketMessage)));
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

