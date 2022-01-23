package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.dto.response.auth.LoginResponse;

public interface AuthService {
    public LoginResponse login(String email, String password);

    public double passwordStrength(String password);
}
