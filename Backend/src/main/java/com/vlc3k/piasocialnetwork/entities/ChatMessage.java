package com.vlc3k.piasocialnetwork.entities;

import com.vlc3k.piasocialnetwork.enums.EPostType;
import lombok.*;

import javax.persistence.*;

@Entity(name = "ChatMessage")
@Table(name = "chat_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_from_id")
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "user_to_id")
    private User userTo;

    @Column(nullable = false, length = 512)
    private String message;

    @Column(nullable = false)
    private long timestamp;
}