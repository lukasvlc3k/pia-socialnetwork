package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.configuration.security.JwtUtils;
import com.vlc3k.piasocialnetwork.dto.response.auth.LoginResponse;
import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.services.AuthService;
import com.vlc3k.piasocialnetwork.services.RoleService;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("authService")
@Getter
@Setter
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final UserService userService;
    private final RoleService roleService;

    @Value("${socialnetwork.app.jwtExpirationMs}")
    private int jwtExpirationMs;


    @Override
    public Optional<LoginResponse> login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var expiration = new Date((new Date()).getTime() + jwtExpirationMs);

        String jwt = jwtUtils.generateJwtToken(authentication, expiration);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var user = userService.getById(userDetails.getId());
        if (user.isEmpty()) {
            return null;
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Optional.of(new LoginResponse(jwt,
                roles, expiration, new UserDto(user.get())));
    }

    @Override
    public double passwordStrength(String password) {
        return password.length();
    }
}
