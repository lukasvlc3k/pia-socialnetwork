package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.entities.FriendRequest;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.entities.UserBlock;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

public interface FriendsService {
    public FriendRequest sendFriendRequest(User toWhom);

    public void acceptFriendRequest(FriendRequest friendRequest) throws AuthenticationException;

    public void rejectFriendRequest(FriendRequest friendRequest);

    public UserBlock blockUser(User userToBlock);

    public void unBlockUser(User userToUnBlock);

    public void unBlockUser(UserBlock block);

    public List<FriendRequest> getFriendRequests();

    public List<FriendRequest> getFriendRequests(User user);

    public List<UserBlock> getBlockedUsers(User user);

    public boolean existFriendship(User userFrom, User userTo);

    public boolean existFriendRequest(User userFrom, User userTo);

    public Optional<FriendRequest> getFriendRequestById(long id);

    public Optional<UserBlock> getUserBlockById(long id);

}
