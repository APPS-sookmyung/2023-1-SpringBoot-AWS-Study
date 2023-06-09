package com.hailey.book.springboot.service.posts;

import com.hailey.book.springboot.domain.posts.Posts;
import com.hailey.book.springboot.domain.posts.PostsRepository;
import com.hailey.book.springboot.web.dto.PostsListResponseDto;
import com.hailey.book.springboot.web.dto.PostsResponseDto;
import com.hailey.book.springboot.web.dto.PostsSaveRequestDto;
import com.hailey.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts=postsRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        posts.update(requestDto.getTitle(),requestDto.getContent());
        return  id;

    }

    // 조회
    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new PostsResponseDto(entity);
    }

    /*
    // 삭제
    // 2주차과제 (2)
    @Transactional
    public Long deleteById(Long id){
        Posts posts=postsRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        postsRepository.deleteById(posts.getId());
        return  id;

    }
    */

    @Transactional
    public void delete (Long id) {
        Posts posts=postsRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        postsRepository.delete(posts); // 지원해주는거 그냥 쓰지 왜만들었지

    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }


}
