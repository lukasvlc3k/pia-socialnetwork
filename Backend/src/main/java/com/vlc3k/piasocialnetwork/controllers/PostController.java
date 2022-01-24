package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.request.post.PostCreateRequest;
import com.vlc3k.piasocialnetwork.dto.response.post.PostDto;
import com.vlc3k.piasocialnetwork.entities.Post;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.services.PostService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    @PostMapping("/")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostCreateRequest postCreateRequest) {
        // todo checks

        if (postCreateRequest.getContent().length() > 8192){
            // to long
            return ResponseEntity.badRequest().body(null);
        }

        if (postCreateRequest.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var post = postService.createNewPost(currentUser, postCreateRequest.getContent(), postCreateRequest.getPostType());

        var postDto = new PostDto(post);
        return ResponseEntity.ok(postDto);
    }

    private Pageable getPageable(String countS) {
        int count = 0;
        try {
            count = Integer.parseInt(countS);
        } catch (Exception ignored) {
        }

        return count < 0 ? Pageable.unpaged() : PageRequest.of(0, count);
    }

    private Optional<Long> getDateTimestamp(String dateS) {
        try {
            long dateLong = Long.parseLong(dateS);
            if (dateLong < 0) {
                return Optional.empty();
            }
            //Date dt = new Date(dateLong);
            return Optional.of(dateLong);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<PostDto>> getPosts(@RequestParam(required = false, defaultValue = "10", name = "count") String countS,
                                                  @RequestParam(required = false, defaultValue = "-1", name = "newerThan") String newerThanS,
                                                  @RequestParam(required = false, defaultValue = "-1", name = "olderThan") String olderThanS
                                                  /*@AuthenticationPrincipal User authorizedUser*/) {
        User authorizedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var pageable = getPageable(countS);

        var newerThanDate = getDateTimestamp(newerThanS);
        var olderThanDate = getDateTimestamp(olderThanS);

        List<Post> posts;
        if (newerThanDate.isEmpty() && olderThanDate.isEmpty()) {
            posts = postService.getAllVisiblePosts(authorizedUser, pageable);
        } else if (newerThanDate.isPresent() && olderThanDate.isEmpty()) {
            posts = postService.getVisiblePostsNewer(newerThanDate.get(), authorizedUser, pageable);
        } else if (newerThanDate.isEmpty() && olderThanDate.isPresent()) {
            posts = postService.getVisiblePostsOlder(olderThanDate.get(), authorizedUser, pageable);
        } else {
            posts = postService.getVisiblePostsNewerOlder(olderThanDate.get(), newerThanDate.get(), authorizedUser, pageable);
        }

        var postsDto = posts.stream().map(PostDto::new).toList();
        return ResponseEntity.ok(postsDto);
    }
}
