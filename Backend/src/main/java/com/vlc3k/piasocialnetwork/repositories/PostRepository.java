package com.vlc3k.piasocialnetwork.repositories;

import com.vlc3k.piasocialnetwork.entities.Post;
import com.vlc3k.piasocialnetwork.entities.Role;
import com.vlc3k.piasocialnetwork.enums.ERole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p inner join p.user where (?1 IN (select f.id from p.user.friends f)) OR p.postType = com.vlc3k.piasocialnetwork.enums.EPostType.ANNOUNCMENT OR p.user.id = ?1 order by p.datePublished desc")
    List<Post> findVisible(Long userId, Pageable pageable);

    @Query("select p from Post p inner join p.user where (?1 IN (select f.id from p.user.friends f)) OR p.postType = com.vlc3k.piasocialnetwork.enums.EPostType.ANNOUNCMENT OR p.user.id = ?1 AND p.datePublished > ?2 order by p.datePublished")
    List<Post> findVisibleNewer(Long userId, Date newerThan, Pageable pageable);

    @Query("select p from Post p inner join p.user where (?1 IN (select f.id from p.user.friends f)) OR p.postType = com.vlc3k.piasocialnetwork.enums.EPostType.ANNOUNCMENT OR p.user.id = ?1 AND p.datePublished < ?2 order by p.datePublished")
    List<Post> findVisibleOlder(Long userId, Date olderThan, Pageable pageable);
}