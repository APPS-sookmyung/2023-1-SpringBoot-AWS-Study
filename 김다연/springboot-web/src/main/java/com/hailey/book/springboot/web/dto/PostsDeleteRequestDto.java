package com.hailey.book.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsDeleteRequestDto {
    private String title;

    @Builder
    public PostsDeleteRequestDto(String title,String content){
        this.title=title;
    }
}

// 필요 없지 않나?
