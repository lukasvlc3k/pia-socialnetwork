package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.configuration.security.JwtUtils;
import com.vlc3k.piasocialnetwork.services.JwtAuthService;
import com.vlc3k.piasocialnetwork.services.RoleService;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("jwtAuthService")
@Getter
@Setter
@RequiredArgsConstructor
public class JwtAuthServiceImpl implements JwtAuthService {

    private final JwtUtils jwtUtils;

    private final UserService userService;
    private final RoleService roleService;
    private final UserDetailsService userDetailsService;

    @Override
    public Optional<AuthResult> authUserByToken(String token) {
        if (token.isEmpty()) {
            return Optional.empty();
        }
        if (!jwtUtils.validateJwtToken(token)) {
            return Optional.empty();
        }

        String username = jwtUtils.getEmailFromJwtToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        var user = userService.getByEmail(username);

        if (user.isPresent()) {
            return Optional.of(new AuthResult(userDetails, user.get()));
        } else {
            return Optional.empty();
        }
    }
}
