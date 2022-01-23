package com.vlc3k.piasocialnetwork.dto.response.auth;

import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
    private String token;
    private String email;
    private List<String> roles;
    private Date expiration;
}
