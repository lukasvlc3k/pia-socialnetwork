package com.vlc3k.piasocialnetwork.dto.response.friends;

import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.entities.UserBlock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBlockDto {
    private UserDto user;
    private long id;

    public UserBlockDto(UserBlock userBlock) {
        this.user = new UserDto(userBlock.getBlockedUser());
        this.id = userBlock.getId();
    }
}
