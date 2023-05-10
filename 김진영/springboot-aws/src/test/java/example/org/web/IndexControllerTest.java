package example.org.web;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IndexControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 메인페이지_로딩(){
        //when
        String body= this.restTemplate.getForObject("/",String.class);

        //then
        assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
        // TestRestTemplate를 통해 "/"로 호출했을때 index.mustache에 포함된 코드들이 있는지 확인
        // 전체 코드를 다 검증할 필요는 x. "스프링 부트로 시작하는 웹 서비스" 문자열이 포함되어있는지만 확인
    }
}
