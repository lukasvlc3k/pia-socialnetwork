package com.vlc3k.piasocialnetwork.dto.response.user;

import com.vlc3k.piasocialnetwork.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private Long id;

    public UserDto(User user) {
        this.name = user.getName();
        this.id = user.getId();
    }
}
