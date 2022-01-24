package com.vlc3k.piasocialnetwork.repositories;

import com.vlc3k.piasocialnetwork.entities.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p inner join p.user where (?1 IN (select f.id from p.user.friends f)) OR p.postType = com.vlc3k.piasocialnetwork.enums.EPostType.ANNOUNCEMENT OR p.user.id = ?1 order by p.timestampPublished desc")
    List<Post> findVisible(Long userId, Pageable pageable);

    @Query("select p from Post p inner join p.user where ((?1 IN (select f.id from p.user.friends f)) OR p.postType = com.vlc3k.piasocialnetwork.enums.EPostType.ANNOUNCEMENT OR p.user.id = ?1) AND p.timestampPublished > ?2 order by p.timestampPublished desc")
    List<Post> findVisibleNewer(Long userId, long newerThan, Pageable pageable);

    @Query("select p from Post p inner join p.user where ((?1 IN (select f.id from p.user.friends f)) OR p.postType = com.vlc3k.piasocialnetwork.enums.EPostType.ANNOUNCEMENT OR p.user.id = ?1) AND p.timestampPublished < ?2 order by p.timestampPublished desc")
    List<Post> findVisibleOlder(Long userId, long olderThan, Pageable pageable);

    @Query("select p from Post p inner join p.user where ((?1 IN (select f.id from p.user.friends f)) OR p.postType = com.vlc3k.piasocialnetwork.enums.EPostType.ANNOUNCEMENT OR p.user.id = ?1) AND p.timestampPublished < ?2 AND p.timestampPublished > ?3 order by p.timestampPublished")
    List<Post> findVisibleNewerOlder(Long userId, long olderThan, long newerThan, Pageable pageable);
}