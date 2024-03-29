package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.request.post.PostCreateRequest;
import com.vlc3k.piasocialnetwork.dto.response.Result;
import com.vlc3k.piasocialnetwork.dto.response.post.PostDto;
import com.vlc3k.piasocialnetwork.entities.Post;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.enums.EPostType;
import com.vlc3k.piasocialnetwork.services.PostService;
import com.vlc3k.piasocialnetwork.utils.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<Result<PostDto>> createPost(@Valid @RequestBody PostCreateRequest postCreateRequest) {
        User currentUser = utils.getCurrentUser();

        if (postCreateRequest.getPostType() == EPostType.ANNOUNCEMENT && !currentUser.isAdmin()) {
            return ResponseEntity.status(403).body(Result.err("User not authorized to create announcements"));
        }
        if (postCreateRequest.getContent().length() > 8192) {
            // to long
            return ResponseEntity.badRequest().body(Result.err("Content too long"));
        }
        if (postCreateRequest.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body(Result.err("Content can not be empty"));
        }

        var post = postService.createNewPost(currentUser, postCreateRequest.getContent(), postCreateRequest.getPostType());

        var postDto = new PostDto(post);
        return ResponseEntity.ok(Result.ok(postDto));
    }


    @GetMapping("")
    public ResponseEntity<List<PostDto>> getPosts(@RequestParam(required = false, defaultValue = "10", name = "count") String countS,
                                                  @RequestParam(required = false, defaultValue = "-1", name = "newerThan") String newerThanS,
                                                  @RequestParam(required = false, defaultValue = "-1", name = "olderThan") String olderThanS
            /*@AuthenticationPrincipal User authorizedUser*/) {
        User currentUser = utils.getCurrentUser();

        var pageable = utils.getPageable(countS);

        var newerThanDate = utils.getDateTimestamp(newerThanS);
        var olderThanDate = utils.getDateTimestamp(olderThanS);

        List<Post> posts;
        if (newerThanDate.isEmpty() && olderThanDate.isEmpty()) {
            posts = postService.getAllVisiblePosts(currentUser, pageable);
        } else if (newerThanDate.isPresent() && olderThanDate.isEmpty()) {
            posts = postService.getVisiblePostsNewer(newerThanDate.get(), currentUser, pageable);
        } else if (newerThanDate.isEmpty() && olderThanDate.isPresent()) {
            posts = postService.getVisiblePostsOlder(olderThanDate.get(), currentUser, pageable);
        } else {
            posts = postService.getVisiblePostsNewerOlder(olderThanDate.get(), newerThanDate.get(), currentUser, pageable);
        }

        var postsDto = posts.stream().map(PostDto::new).toList();
        return ResponseEntity.ok(postsDto);
    }
}
