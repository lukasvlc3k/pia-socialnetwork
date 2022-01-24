package com.vlc3k.piasocialnetwork.services.impl;

import com.vlc3k.piasocialnetwork.entities.Post;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.EPostType;
import com.vlc3k.piasocialnetwork.repositories.PostRepository;
import com.vlc3k.piasocialnetwork.services.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("postService")
@Getter
@Setter
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository repository;

    @Override
    public List<Post> getAllVisiblePosts(User user, Pageable pageable) {
        return repository.findVisible(user.getId(), pageable);
    }

    @Override
    public List<Post> getAllVisiblePosts(Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.getAllVisiblePosts(currentUser, pageable);
    }

    @Override
    public Post createNewPost(User user, String content, EPostType postType) {
        var post = Post.builder()
                .postType(postType)
                .content(content)
                .timestampPublished(new Date().getTime())
                .user(user)
                .build();
        post = repository.save(post);
        return post;
    }

    @Override
    public List<Post> getVisiblePostsNewer(long datetime, User user, Pageable pageable) {
        return repository.findVisibleNewer(user.getId(), datetime, pageable);
    }

    @Override
    public List<Post> getVisiblePostsNewer(long datetime, Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.getVisiblePostsNewer(datetime, currentUser, pageable);
    }

    @Override
    public List<Post> getVisiblePostsNewerOlder(long olderThan, long newerThan, User user, Pageable pageable) {
        return repository.findVisibleNewerOlder(user.getId(), olderThan, newerThan, pageable);
    }

    @Override
    public List<Post> getVisiblePostsNewerOlder(long olderThan, long newerThan, Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getVisiblePostsNewerOlder(olderThan, newerThan, currentUser, pageable);
    }

    @Override
    public List<Post> getVisiblePostsOlder(long datetime, User user, Pageable pageable) {
        return repository.findVisibleOlder(user.getId(), datetime, pageable);
    }

    @Override
    public List<Post> getVisiblePostsOlder(long datetime, Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.getVisiblePostsOlder(datetime, currentUser, pageable);
    }
}
