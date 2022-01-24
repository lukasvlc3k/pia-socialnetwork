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
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;

    @Override
    public Optional<Role> getByName(ERole name) {
        return repo.findByName(name);
    }

}
