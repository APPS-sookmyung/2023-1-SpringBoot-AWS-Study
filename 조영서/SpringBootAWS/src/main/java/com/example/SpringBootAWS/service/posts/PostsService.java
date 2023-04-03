package com.example.SpringBootAWS.service.posts;

import com.example.SpringBootAWS.domain.posts.Posts;
import com.example.SpringBootAWS.domain.posts.PostsRepository;
import com.example.SpringBootAWS.web.dto.PostsResponseDto;
import com.example.SpringBootAWS.web.dto.PostsSaveRequestDto;
import com.example.SpringBootAWS.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    /*@Transactional //과제
    public void delete(Long id){
        Posts posts = postsRepository.findById(id) //DB에서 해당 글 찾기
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)); //예외처리
        postsRepository.delete(posts); //게시글 찾으면 delete()메섣를 이용해 글 삭제
    }*/

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }


}