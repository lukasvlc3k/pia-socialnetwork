package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.enums.ERole;
import com.vlc3k.piasocialnetwork.repositories.RoleRepository;
import com.vlc3k.piasocialnetwork.services.RoleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("roleService")
@Getter
@Setter
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> getByName(ERole name) {
        return roleRepository.findByName(name);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role addRole(ERole name) {
        var role = Role.builder()
                .name(name)
                .build();

        return roleRepository.save(role);
    }
}
