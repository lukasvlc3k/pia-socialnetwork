package com.vlc3k.piasocialnetwork.dto.request.friends;

import com.vlc3k.piasocialnetwork.enums.EFriendRequestResolveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestResolve {
    private EFriendRequestResolveType resolveType;
}