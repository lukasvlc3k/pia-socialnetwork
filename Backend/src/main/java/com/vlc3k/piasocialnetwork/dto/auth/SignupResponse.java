package com.vlc3k.piasocialnetwork.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
    private boolean ok;
    private String message;
}
