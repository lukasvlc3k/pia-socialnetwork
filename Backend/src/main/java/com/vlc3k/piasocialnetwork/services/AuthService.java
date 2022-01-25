package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.dto.response.auth.LoginResponse;
import com.vlc3k.piasocialnetwork.services.impl.AuthResult;

import java.util.Optional;

public interface AuthService {
    public Optional<LoginResponse> login(String email, String password);

    public double passwordStrength(String password);

}
