package com.vlc3k.piasocialnetwork.entities;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "User")
@Table(name = "user_sn") // name user is reserved by PostgreSQL
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password; // b-crypt

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id")
            }
    )
    private Set<Role> roles = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_friends",
            joinColumns = {
                    @JoinColumn(name = "user1_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user2_id")
            }
    )
    private Set<User> friends = new HashSet<>();

    @OneToMany(mappedBy = "userTo")
    private Set<FriendRequest> receivedFriendRequests = new HashSet<>();

    @OneToMany(mappedBy = "userFrom")
    private Set<FriendRequest> sentFriendRequests = new HashSet<>();

    @OneToMany(mappedBy = "blockedBy")
    private Set<UserBlock> blockedUsers = new HashSet<>();


    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
