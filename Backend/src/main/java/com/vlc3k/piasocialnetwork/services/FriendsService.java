package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.entities.FriendRequest;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.entities.UserBlock;

import java.util.List;

public interface FriendsService {
    public FriendRequest SendFriendRequest(User toWhom);
    public void AcceptFriendRequest(FriendRequest friendRequest) throws Exception;
    public void RejectFriendRequest(FriendRequest friendRequest) throws Exception;

    public UserBlock BlockUser(User userToBlock);
    public void UnBlockUser(User userToUnBlock);

    public List<FriendRequest> getFriendRequests();
    public List<FriendRequest> getFriendRequests(User user);

    public List<UserBlock> getBlockedUsers();
}
