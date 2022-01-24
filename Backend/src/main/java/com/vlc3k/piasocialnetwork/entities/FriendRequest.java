package com.vlc3k.piasocialnetwork.entities;

import lombok.*;

import javax.persistence.*;

@Entity(name = "FriendRequest")
@Table(name = "friend_request")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FriendRequest {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_from_id", referencedColumnName = "id")
    private User userFrom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_to_id", referencedColumnName = "id")
    private User userTo;
}