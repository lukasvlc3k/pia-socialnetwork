package com.vlc3k.piasocialnetwork.dto.auth;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private List<String> roles;
    private Date expiration;
}
