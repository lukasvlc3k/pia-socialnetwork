package com.vlc3k.piasocialnetwork.dto.response.auth;

import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
    private String token;
    private List<String> roles;
    private Date expiration;

    private UserDto user;
}
