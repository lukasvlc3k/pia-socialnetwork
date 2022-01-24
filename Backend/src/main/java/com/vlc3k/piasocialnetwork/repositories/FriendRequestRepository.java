package com.vlc3k.piasocialnetwork.repositories;

import com.vlc3k.piasocialnetwork.entities.FriendRequest;
import com.vlc3k.piasocialnetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    public List<FriendRequest> findByUserFrom(User userFrom);

    public List<FriendRequest> findByUserTo(User userTo);
}
