package example.org.web;


import example.org.config.auth.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)  // JUnit5에서는 RunWith가 ExtendWith로 변경됨.
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class)
        })
public class HelloControllerTest {
    @Autowired //스프링이 관리하는 bean을 주입받음.
    private MockMvc mvc; //웹 API를 테스트할 때 사용. 스프링 MVC 테스트의 시작점.

    @Test
    public void hello가_리턴된다() throws Exception{
        String hello="hello";

        mvc.perform(get("/hello")) //MockMvc를 통해 /hello 주소로 HTTP GET 요청을 함.
                .andExpect(status().isOk()) //mvc.perform의 결과, HTTP Header의 Status를 검증함. (202,404,500의 상태 검증; 여기선 OK, 즉 200인지 아닌지를 검증)
                .andExpect(content().string(hello)); //mvc.perform의 결과를 검증. 응답 본문 내용 검증. Controller에서 "hello"를 리턴하기 위해 이 값이 맞는지 검증.
    }

    @Test
    public void helloDto가_리턴된다() throws Exception{
        String name="hello";
        int amount=1000;

        mvc.perform(
                        get("/hello/dto")
                                .param("name",name) //param: API 테스트할 때 사용될 요청 파라미터를 설정. 값은 String만 허용. (문자열 변경 필요할수도)
                                .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(name))) //jsonPath: JSON 응답값을 필드별로 검증할 수 있는 메소드. $을 기준으로 필드명을 명시
                .andExpect(jsonPath("$.amount",is(amount)));
    }

    @Test
    public void assignment가_리턴된다() throws Exception{
        String assign="first-assignment";

        mvc.perform(get("/hello/assignment"))
                .andExpect(status().isOk())
                .andExpect(content().string(assign));
    }
}
