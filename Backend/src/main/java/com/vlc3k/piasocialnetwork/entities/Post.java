package com.vlc3k.piasocialnetwork.entities;

import com.vlc3k.piasocialnetwork.enums.EPostType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Post")
@Table(name = "post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EPostType postType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 8192)
    private String content;

    @Column(nullable = false)
    private long timestampPublished;
}