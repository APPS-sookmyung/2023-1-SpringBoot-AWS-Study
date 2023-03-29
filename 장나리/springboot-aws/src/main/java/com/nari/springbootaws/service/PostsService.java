package com.nari.springbootaws.service;

import com.nari.springbootaws.domain.posts.Posts;
import com.nari.springbootaws.domain.posts.PostsRepository;
import com.nari.springbootaws.web.dto.PostsResponseDto;
import com.nari.springbootaws.web.dto.PostsSaveRequestDto;
import com.nari.springbootaws.web.dto.PostsUpdateRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        posts.update(requestDto.getTitle(),requestDto.getContent());

        return id;
    }
    @Transactional
    public Long delete(Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        postsRepository.delete(posts);

        return id;
    }


    public PostsResponseDto findById (Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        return new PostsResponseDto(entity);
    }
}
