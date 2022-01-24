package com.vlc3k.piasocialnetwork.repositories;

import com.vlc3k.piasocialnetwork.entities.FriendRequest;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.entities.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    Optional<UserBlock> findByBlockedByAndBlockedUser(User blockedBy, User blockedUser);

    boolean existsByBlockedByAndBlockedUser(User blockedBy, User blockedUser);

    List<UserBlock> findByBlockedBy(User blockedBy);
}