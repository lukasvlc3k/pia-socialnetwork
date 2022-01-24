package com.vlc3k.piasocialnetwork.dto.response.friends;

import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.entities.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestDto {
    private long requestId;
    private UserDto userFrom;
    private UserDto userTo;

    public FriendRequestDto(FriendRequest friendRequest) {
        this.requestId = friendRequest.getId();
        this.userFrom = new UserDto(friendRequest.getUserFrom());
        this.userTo = new UserDto(friendRequest.getUserTo());
    }
}
