package example.org.web;

import example.org.config.auth.LoginUser;
import example.org.config.auth.dto.SessionUser;
import example.org.service.posts.PostsService;
import example.org.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){ //Model: 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장 가능
        model.addAttribute("posts",postsService.findAllDesc()); //findAllDesc()에서 가져온 결과를 posts로 index.mustache에 전달
        //SessionUser user=(SessionUser) httpSession.getAttribute("user");
        if(user!=null){
            model.addAttribute("memberName",user.getName());
            //chap5.3 기존의 userName 을 memberName으로 변경 : Windows에서 userName 충돌이 일어남
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save"; // /posts/save를 호출하면 posts-save.mustache를 호출하는 메소드 추가
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto=postsService.findById(id);
        model.addAttribute("post",dto);

        return "posts-update";
    }
}
