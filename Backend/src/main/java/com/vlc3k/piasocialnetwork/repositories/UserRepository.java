package com.vlc3k.piasocialnetwork.repositories;

import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.ERole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    Boolean existsByEmailIgnoreCase(String email);

    @Query("select u from User u where lower(u.name) LIKE lower(concat('%', ?2,'%')) AND (?1 NOT IN (select f.id from u.friends f)) AND (?1 NOT IN (select b.blockedUser from u.blockedUsers b)) AND NOT u.id = ?1 order by u.name desc")
    List<User> findRelevantUsers(long searcherUserId, String search, Pageable pageable);

    @Query("select u from User u inner join u.roles r where r.name=?1")
    List<User> findUsersInRole(ERole role);

    @Query("select u from User u where u.isOnline=?1")
    List<User> findByIsOnline(boolean online);
}