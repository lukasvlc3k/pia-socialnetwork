package com.vlc3k.piasocialnetwork.controllers;

import com.vlc3k.piasocialnetwork.dto.request.post.PostCreateRequest;
import com.vlc3k.piasocialnetwork.dto.response.post.PostDto;
import com.vlc3k.piasocialnetwork.entities.User;
import com.vlc3k.piasocialnetwork.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostCreateRequest postCreateRequest) {
        // todo checks
        if (postCreateRequest.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var post = postService.createNewPost(currentUser, postCreateRequest.getContent(), postCreateRequest.getPostType());

        var postDto = new PostDto(post);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<PostDto>> getPosts(@RequestParam(required = false, defaultValue = "10", name = "count") String countS) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int count = 0;
        try {
            count = Integer.parseInt(countS);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(null);
        }

        Pageable pageable = count < 0 ? Pageable.unpaged() : PageRequest.of(0, count);
        var posts = postService.getAllVisiblePosts(currentUser, pageable);


        var postsDto = posts.stream().map(PostDto::new).toList();

        return ResponseEntity.ok(postsDto);
    }
}
