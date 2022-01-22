package com.vlc3k.piasocialnetwork.entities;

import javax.persistence.*;

@Entity(name = "FriendRequest")
@Table(name = "friend_request")
public class FriendRequest {
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


    public long getId() {
        return id;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }
}