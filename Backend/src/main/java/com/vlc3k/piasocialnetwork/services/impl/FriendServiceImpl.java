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

import javax.naming.AuthenticationException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    public FriendRequest sendFriendRequest(User toWhom) {
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
    public void acceptFriendRequest(FriendRequest friendRequest) throws AuthenticationException {
        var currentUser = userService.getLoggedUserUpdated();
        if (currentUser.isEmpty()) {
            throw new AuthenticationException("Logged user not found");
        }

        friendRequest.getUserFrom().getFriends().add(currentUser.get());
        currentUser.get().getFriends().add(friendRequest.getUserFrom());

        friendRequestRepository.delete(friendRequest);

        userRepository.save(currentUser.get());
        userRepository.save(friendRequest.getUserTo());

        // find opposite direction friend requests and delete them
        var opposite = friendRequestRepository.findByUserToAndUserFrom(friendRequest.getUserFrom(), friendRequest.getUserTo());
        friendRequestRepository.deleteAll(opposite);
    }

    @Override
    @Transactional
    public void rejectFriendRequest(FriendRequest friendRequest) {
        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public UserBlock blockUser(User userToBlock) {
        var currentUser = utils.getCurrentUser();

        var userBlock = UserBlock.builder()
                .blockedUser(userToBlock)
                .blockedBy(currentUser)
                .build();

        userBlock = userBlockRepository.save(userBlock);

        // find all friend requests from that user and remove them
        var requests = friendRequestRepository.findByUserToAndUserFrom(currentUser, userToBlock);
        friendRequestRepository.deleteAll(requests);

        return userBlock;
    }

    @Override
    public void unBlockUser(User userToUnBlock) {
        var currentUser = utils.getCurrentUser();

        var userBlock = userBlockRepository.findByBlockedByAndBlockedUser(
                currentUser, userToUnBlock);

        if (userBlock.isPresent()) {
            userBlockRepository.delete(userBlock.get());
        }
        // else nothing to do, user not blocked;
    }

    @Override
    public void unBlockUser(UserBlock block) {
        userBlockRepository.delete(block);
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
    public List<UserBlock> getBlockedUsers(User user) {
        return userBlockRepository.findByBlockedBy(user);
    }

    @Override
    public boolean existFriendship(User userFrom, User userTo) {
        return userFrom.getFriends().stream().anyMatch(u -> u.getId() == userTo.getId());
    }

    @Override
    public boolean existFriendRequest(User userFrom, User userTo) {
        return (long) friendRequestRepository.findByUserToAndUserFrom(userTo, userFrom).size() > 0;
    }

    @Override
    public Optional<FriendRequest> getFriendRequestById(long id) {
        return friendRequestRepository.findById(id);
    }

    @Override
    public Optional<UserBlock> getUserBlockById(long id) {
        return userBlockRepository.findById(id);
    }

}
