package com.vlc3k.piasocialnetwork.entities;

import com.vlc3k.piasocialnetwork.enums.ERole;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "User")
@Table(name = "user_sn") // name user is reserved by PostgreSQL
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Setter(AccessLevel.NONE)
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

    @Column(nullable = false)
    private boolean isOnline;

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
    @Builder.Default
    private Set<Role> roles = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @Builder.Default
    @JoinTable(
            name = "user_friends",
            joinColumns = {
                    @JoinColumn(name = "user1_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user2_id", referencedColumnName = "id")
            }
    )
    private Set<User> friends = new java.util.LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "userTo")
    private Set<FriendRequest> receivedFriendRequests = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "userFrom")
    private Set<FriendRequest> sentFriendRequests = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "blockedBy")
    private Set<UserBlock> blockedUsers = new HashSet<>();


    public boolean isAdmin() {
        return this.getRoles().stream().anyMatch(r -> r.getName() == ERole.ROLE_ADMIN);
    }
}
