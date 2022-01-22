package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.enums.ERole;
import com.vlc3k.piasocialnetwork.repositories.RoleRepository;
import com.vlc3k.piasocialnetwork.services.RoleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service("roleService")
@Getter
@Setter
@RequiredArgsConstructor
@RequestScope
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;

    @Override
    public Optional<Role> tryGetByName(ERole name) {
        return repo.findByName(name);
    }

    @Override
    public Role getByName(ERole name) {
        var role = tryGetByName(name);
        if (role.isEmpty()) {
            throw new NotFoundException("Name not found");
        }

        return role.get();
    }
}
