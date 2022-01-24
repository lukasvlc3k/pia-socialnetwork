package com.vlc3k.piasocialnetwork.dto.response.user;

import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.ECanBeAddedToFriendsType;
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
    private String email;
    private ECanBeAddedToFriendsType canBeAddedToFriendsType;
    private boolean isOnline;

    public UserDto(User user) {
        this.name = user.getName();
        this.id = user.getId();
        this.email = user.getEmail();
        this.isOnline = user.isOnline();
    }
}
