package com.vlc3k.piasocialnetwork.repositories;

import com.vlc3k.piasocialnetwork.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("select u from User u where lower(u.name) LIKE lower(concat('%', ?2,'%')) AND (?1 NOT IN (select f.id from u.friends f)) AND (?1 NOT IN (select b.blockedUser from u.blockedUsers b)) order by u.name desc")
    List<User> findRelevantUsers(long searcherUserId, String search, Pageable pageable);
}