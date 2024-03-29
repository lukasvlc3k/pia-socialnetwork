package com.vlc3k.piasocialnetwork.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.services.ChatMessageService;
import com.vlc3k.piasocialnetwork.services.JwtAuthService;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
import java.util.List;

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger LOG = LoggerFactory.getLogger(SocketHandler.class);

    private final HashMap<String, AuthorizedSession> authorizedSessions = new HashMap<>();

    private final JwtAuthService jwtAuthService;
    private final UserService userService;
    private final ChatMessageService chatMessageService;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        if (session.isOpen()) {
            session.close(CloseStatus.PROTOCOL_ERROR);
        }
        afterConnectionClosed(session, CloseStatus.PROTOCOL_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var authorizedSession = authorizedSessions.getOrDefault(session.getId(), null);
        if (authorizedSession != null) {
            onUserOnlineChange(authorizedSession.getUser(), false);
        }

        authorizedSessions.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        val socketMessage = objectMapper.readValue(jsonTextMessage.getPayload(), SocketMessage.class);

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
            // Not authorized
            sendMessage(session,
                    SocketMessage.builder()
                            .type(SocketMessageType.AUTHORIZATION)
                            .content("FAIL")
                            .build()
            );
            session.close();
            return;
        }

        switch (socketMessage.getType()) {
            case AUTHORIZATION -> onUserOnlineChange(authorizedSession.getUser(), true);
            case CHAT -> {
                var chatMessage = objectMapper.readValue(jsonTextMessage.getPayload(), SocketMessageChat.class);
                handleChatMessage(authorizedSession, chatMessage.getContent());
            }
        }
    }

    private void handleChatMessage(AuthorizedSession authorizedSession, SocketChatMessage chatMessage) {
        if (chatMessage.getUserFromId() != authorizedSession.getUser().getId()) {
            // invalid user from
            return;
        }

        var recipient = userService.getById(chatMessage.getUserToId());
        if (recipient.isEmpty()) {
            // recipient does not exist
            return;
        }

        String messageText = chatMessage.getMessage();
        if (messageText.length() > 200) {
            // message will be truncated (user was warned)
            messageText = messageText.substring(0, 200);
        }

        var message = chatMessageService.AddMessage(authorizedSession.getUser(), recipient.get(), messageText);

        var socketMessage = new SocketChatMessage(message);


        try {
            // send message back to sender
            var activeSessions = getSessionsOfUser(authorizedSession.getUser().getId());
            for (var session : activeSessions) {
                sendMessage(session.getSession(), new SocketMessage(SocketMessageType.CHAT, socketMessage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // send message to recipients
            var activeSessions = getSessionsOfUser(recipient.get().getId());
            for (var session : activeSessions) {
                sendMessage(session.getSession(), new SocketMessage(SocketMessageType.CHAT, socketMessage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onUserOnlineChange(User user, boolean isOnline) {
        userService.changeIsOnline(user, isOnline);

        var activeSessions = getSessionsOfUser(user.getId()).size();
        if (!isOnline && activeSessions > 0) {
            return;
        }

        // get "fresh" user instance with friends
        var usr = userService.getById(user.getId());
        if (usr.isEmpty()) {
            // user does not exist (was deleted in the meantime?)
            return;
        }
        var friendIds = usr.get().getFriends().stream().map(User::getId).toList();

        authorizedSessions.forEach((key, value) -> {
            try {
                var session = value.getSession();
                if (friendIds.contains(value.getUser().getId()) && session.isOpen()) {
                    sendMessage(session, new SocketMessage(isOnline ? SocketMessageType.USER_JOIN : SocketMessageType.USER_LEAVE, user.getId()));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private List<AuthorizedSession> getSessionsOfUser(long userId) {
        return authorizedSessions.values().stream().filter(
                        authorizedSession -> authorizedSession.getSession().isOpen() && authorizedSession.getUser().getId() == userId)
                .toList();
    }

    private void sendMessage(WebSocketSession webSocketSession, SocketMessage socketMessage) throws IOException {
        if (!webSocketSession.isOpen()) {
            return;
        }

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

