package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.services.impl.AuthResult;

import java.util.Optional;

public interface JwtAuthService {
    public Optional<AuthResult> authUserByToken(String token);
}
