package com.seohee.springboot;

import com.seohee.springboot.config.auth.SecurityConfig;
import com.seohee.springboot.web.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class) // 스프링 부트 테스트와 JUnit 사이에 연결자 역할
@WebMvcTest(controllers = HelloController.class, // Web에 집중할 수 있는 어노테이션
        excludeFilters = { // 스캔 대상에서 SecurityConfig 제거
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })

public class HelloControllerTest {

    @Autowired // 스프링이 관리하는 빈(Bean) 주입받음
    private MockMvc mvc; // MVC 테스트의 시작점

    @WithMockUser(roles = "USER") // 가짜로 인증된 사용자 생성
    @Test
    public void assignment가_리턴된다() throws Exception {
        String hello = "first-assignment";

        mvc.perform(get("/hello/assignment")) // MockMvc를 통해 /hello/assignment 주소로 HTTP GET 요청
                .andExpect(status().isOk()) // mvc.perform의 결과 검증 + HTTP Header의 Status 검증
                .andExpect(content().string(hello)); // mvc.perform의 결과를 검증
    }

    @WithMockUser(roles = "USER") // 가짜로 인증된 사용자 생성
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name) // API 테스트할 때 사용될 요청 파라미터를 설정 (String만 허용)
                        .param("amount", String.valueOf(amount))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name))) // JSON 응답값을 검증하는 메소드, $를 기준으로 필드명 제시
                .andExpect(jsonPath("$.amount", is(amount)));

    }


}