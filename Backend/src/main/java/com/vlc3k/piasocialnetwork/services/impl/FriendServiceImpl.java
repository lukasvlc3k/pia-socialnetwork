package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.entities.FriendRequest;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.entities.UserBlock;
import com.vlc3k.piasocialnetwork.repositories.FriendRequestRepository;
import com.vlc3k.piasocialnetwork.repositories.UserBlockRepository;
import com.vlc3k.piasocialnetwork.repositories.UserRepository;
import com.vlc3k.piasocialnetwork.services.FriendsService;
import com.vlc3k.piasocialnetwork.services.UserService;
import com.vlc3k.piasocialnetwork.utils.utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("friendService")
@Getter
@Setter
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendsService {
    private final UserService userService;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final UserBlockRepository userBlockRepository;

    @Override
    public FriendRequest SendFriendRequest(User toWhom) {
        var currentUser = utils.getCurrentUser();
        var fr = FriendRequest.builder()
                .userFrom(currentUser)
                .userTo(toWhom)
                .build();

        fr = friendRequestRepository.save(fr);
        return fr;
    }

    @Override
    @Transactional
    public void AcceptFriendRequest(FriendRequest friendRequest) throws Exception {
        var currentUser = utils.getCurrentUser();
        if (friendRequest.getUserTo().getId() != currentUser.getId()) {
            throw new Exception("This request is not for the current user");
        }

        friendRequest.getUserFrom().getFriends().add(currentUser);
        currentUser.getFriends().add(friendRequest.getUserTo());

        friendRequestRepository.delete(friendRequest);

        userRepository.save(currentUser);
        userRepository.save(friendRequest.getUserTo());
    }

    @Override
    @Transactional
    public void RejectFriendRequest(FriendRequest friendRequest) throws Exception {
        var currentUser = utils.getCurrentUser();
        if (friendRequest.getUserTo().getId() != currentUser.getId()) {
            throw new Exception("This request is not for the current user");
        }

        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public UserBlock BlockUser(User userToBlock) {
        var currentUser = utils.getCurrentUser();

        var userBlock = UserBlock.builder()
                .blockedUser(userToBlock)
                .blockedBy(currentUser)
                .build();

        userBlock = userBlockRepository.save(userBlock);
        return userBlock;
    }

    @Override
    public void UnBlockUser(User userToUnBlock) {
        var currentUser = utils.getCurrentUser();

        var userBlock = userBlockRepository.findByBlockedByAndBlockedUser(
                currentUser, userToUnBlock);

        if (userBlock.isPresent()) {
            userBlockRepository.delete(userBlock.get());
        }
        // else nothing to do, user not blocked;
    }

    @Override
    public List<FriendRequest> getFriendRequests() {
        var currentUser = utils.getCurrentUser();
        return getFriendRequests(currentUser);
    }

    @Override
    public List<FriendRequest> getFriendRequests(User user) {
        return friendRequestRepository.findByUserTo(user);
    }

    @Override
    public List<UserBlock> getBlockedUsers() {
        var currentUser = utils.getCurrentUser();
        return userBlockRepository.findByBlockedBy(currentUser);
    }
}
