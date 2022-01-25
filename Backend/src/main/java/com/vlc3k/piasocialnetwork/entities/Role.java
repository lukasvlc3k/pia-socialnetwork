package com.vlc3k.piasocialnetwork.entities;

import com.vlc3k.piasocialnetwork.enums.ERole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Role")
@Table(name = "role")
@Getter
@Setter
public class Role {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERole name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return id == role.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
