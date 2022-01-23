package com.vlc3k.piasocialnetwork.dto.request.post;

import com.vlc3k.piasocialnetwork.enums.EPostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {
    @NotEmpty
    @NotNull
    private String content;

    private EPostType postType;
}
