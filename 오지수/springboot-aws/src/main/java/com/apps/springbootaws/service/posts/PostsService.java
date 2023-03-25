package com.apps.springbootaws.service.posts;

import com.apps.springbootaws.domain.posts.Posts;
import com.apps.springbootaws.domain.posts.PostsRepository;
import com.apps.springbootaws.web.dto.PostsResponseDto;
import com.apps.springbootaws.web.dto.PostsSaveRequestDto;
import com.apps.springbootaws.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)); // 찾아낸 게시글이 없다면 오류 처리.

        // 영속성 컨텍스트에서 객체를 관리해줌. 따로 update 쿼리를 날릴 필요 없음.
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(entity);
    }
}
