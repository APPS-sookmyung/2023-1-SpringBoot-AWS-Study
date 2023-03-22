package com.seohee.springboot.web;

import com.seohee.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // JSON을 반환하는 컨트롤러 생성
public class HelloController {

//    @GetMapping("/hello") // Get의 요청을 받을 수 있는 API 만들어줌
//    public String hello(){
//        return "hello";
//    }

//    @GetMapping("/hello/dto")
//    public HelloResponseDto helloDto(@RequestParam("name") String name,
//                                     @RequestParam("amount") int amount){
//        return new HelloResponseDto(name, amount);
//    }

    @GetMapping("/hello/assignment")
    public String hello(){
        return "first-assignment";
    }
}
