package com.example.SpringBootAWS.web;

import com.example.SpringBootAWS.service.posts.PostsService;
import com.example.SpringBootAWS.web.dto.PostsResponseDto;
import com.example.SpringBootAWS.web.dto.PostsSaveRequestDto;
import com.example.SpringBootAWS.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto); }

    //과제....
    /*@DeleteMapping("/api/v1/posts/{id}") //지수 과제....
    public void delete(@PathVariable Long id) {
        postsService.delete(id); } //파라미터로 넘어온 게시글의 ID 값을 PostService의 delete()로 넘기기 */

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }


}