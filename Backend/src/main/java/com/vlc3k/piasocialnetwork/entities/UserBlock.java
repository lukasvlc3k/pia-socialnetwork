package com.vlc3k.piasocialnetwork.entities;

import javax.persistence.*;

@Entity(name = "UserBlock")
@Table(name = "user_blocks")
public class UserBlock {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "blocked_by_id", referencedColumnName = "id")
    private User blockedBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "blocked_user_id", referencedColumnName = "id")
    private User blockedUser;

    public long getId() {
        return id;
    }

    public User getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(User blockedBy) {
        this.blockedBy = blockedBy;
    }

    public User getBlockedUser() {
        return blockedUser;
    }

    public void setBlockedUser(User blockedUser) {
        this.blockedUser = blockedUser;
    }
}