package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.enums.ERole;

import java.util.Optional;

public interface RoleService {
    Role getByName(ERole name);
    Optional<Role> tryGetByName(ERole name);
}
