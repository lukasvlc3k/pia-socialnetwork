package com.vlc3k.piasocialnetwork.entities;

import javax.persistence.*;

@Entity(name = "Role")
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false)
    private String name;


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
