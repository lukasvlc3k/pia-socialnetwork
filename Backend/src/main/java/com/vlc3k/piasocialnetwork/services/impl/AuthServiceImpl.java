package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.configuration.security.JwtUtils;
import com.vlc3k.piasocialnetwork.dto.response.auth.LoginResponse;
import com.vlc3k.piasocialnetwork.services.AuthService;
import com.vlc3k.piasocialnetwork.services.RoleService;
import com.vlc3k.piasocialnetwork.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;
import java.util.List;
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
    public LoginResponse login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var expiration = new Date((new Date()).getTime() + jwtExpirationMs);

        String jwt = jwtUtils.generateJwtToken(authentication, expiration);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new LoginResponse(jwt,
                userDetails.getEmail(),
                roles, expiration);
    }

    @Override
    public double passwordStrength(String password) {
        return password.length();
    }
}
