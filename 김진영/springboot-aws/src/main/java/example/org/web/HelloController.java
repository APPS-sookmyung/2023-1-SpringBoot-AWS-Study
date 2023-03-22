package example.org.web;

import example.org.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //컨트롤러를 JSON을 반환하는 컨트롤러로 만들어줌.
public class HelloController {
    @GetMapping("/hello") //HTTP Method인 Get의 요청을 받을 수 있는 API를 만들어줌.
    public String hello(){ // /hello로 요청이 오면 문자열 hello를 반환하는 기능의 코드.
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name,
                                     @RequestParam("amount") int amount){
        return new HelloResponseDto(name,amount);
    }

    @GetMapping("/hello/assignment")
    public String assign(){
        return "first-assignment";
    }
}
