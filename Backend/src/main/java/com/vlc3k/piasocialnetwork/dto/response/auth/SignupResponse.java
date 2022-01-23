package com.vlc3k.piasocialnetwork.dto.response.auth;

import com.vlc3k.piasocialnetwork.enums.ESignupState;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
    private ESignupState state;
    private String message;

    public static SignupResponse Error(ESignupState errCode){
        return Error(errCode, "");
    }
    public static SignupResponse Error(ESignupState errCode, String message){
        return SignupResponse.builder()
                .state(errCode)
                .message(message)
                .build();
    }
}
