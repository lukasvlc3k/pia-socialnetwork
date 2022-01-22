package com.vlc3k.piasocialnetwork.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    public String email;
    public String name;
    public String password;
}
