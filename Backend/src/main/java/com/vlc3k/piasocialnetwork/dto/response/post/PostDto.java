package com.vlc3k.piasocialnetwork.dto.response.post;

import com.vlc3k.piasocialnetwork.dto.response.user.UserDto;
import com.vlc3k.piasocialnetwork.entities.Post;
import com.vlc3k.piasocialnetwork.enums.EPostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String content;
    private UserDto user;
    private Date publishedDate;
    private EPostType postType;
    private Long postId;

    public PostDto(Post post) {
        this.content = post.getContent();
        this.publishedDate = post.getDatePublished();
        this.postType = post.getPostType();
        this.postId = post.getId();

        this.user = new UserDto(post.getUser());
    }
}
