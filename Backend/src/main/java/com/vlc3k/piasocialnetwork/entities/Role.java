package com.vlc3k.piasocialnetwork.entities;

import com.vlc3k.piasocialnetwork.enums.ERole;

import javax.persistence.*;

@Entity(name = "Role")
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERole name;


    public long getId() {
        return id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
