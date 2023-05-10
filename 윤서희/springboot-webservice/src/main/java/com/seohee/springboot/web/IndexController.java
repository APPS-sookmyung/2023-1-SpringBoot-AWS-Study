package com.seohee.springboot.web;

import com.seohee.springboot.config.auth.LoginUser;
import com.seohee.springboot.config.auth.dto.SessionUser;
import com.seohee.springboot.service.posts.PostsService;
import com.seohee.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) { // findAllDesc()로 가져온 결과를 posts로 index.mustache에 전달
        // 어느 컨트롤러든 @LoginUser 만 사용하면 세션 정보를 가져올 수 있음
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
            return "posts-save"; }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
