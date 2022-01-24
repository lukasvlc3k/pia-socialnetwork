package com.vlc3k.piasocialnetwork.services;

import com.vlc3k.piasocialnetwork.entities.Post;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.EPostType;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface PostService {
    List<Post> getAllVisiblePosts(User user, Pageable pageable);

    List<Post> getAllVisiblePosts(Pageable pageable); // current user

    Post createNewPost(User user, String content, EPostType postType);

    List<Post> getVisiblePostsNewer(long datetime, User user, Pageable pageable);

    List<Post> getVisiblePostsNewer(long datetime, Pageable pageable);


    List<Post> getVisiblePostsNewerOlder(long olderThan, long newerThan, User user, Pageable pageable);

    List<Post> getVisiblePostsNewerOlder(long olderThan, long newerThan, Pageable pageable);


    List<Post> getVisiblePostsOlder(long datetime, User user, Pageable pageable);

    List<Post> getVisiblePostsOlder(long datetime, Pageable pageable);
}
