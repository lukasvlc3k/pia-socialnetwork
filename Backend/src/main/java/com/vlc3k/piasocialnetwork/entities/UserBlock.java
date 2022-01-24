package com.vlc3k.piasocialnetwork.entities;

import lombok.*;

import javax.persistence.*;

@Entity(name = "UserBlock")
@Table(name = "user_blocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBlock {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "blocked_by_id", nullable = false, referencedColumnName = "id")
    private User blockedBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "blocked_user_id", referencedColumnName = "id")
    private User blockedUser;
}