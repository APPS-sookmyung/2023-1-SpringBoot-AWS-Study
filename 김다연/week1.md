## **1주차 과제 필기내용**

### 1) HelloController 코드 정리

```java
@RunWith(SpringRunner.class)  
-> 스프링부트 테스트와 JUnit 사이의 연결자 역할 (SpringRunner라는 스프링 실행자를 사용하도록 한다.)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {
    @Autowired
    private MockMvc mvc;
    -> 웹 API를 테스트 할때 사용하며 스프링 MVC테스터의 시작점

    @Test
    public void hello가_리턴된다() throws Exception{
        String hello ="hello";

        mvc.perform(get("/hello"))
        -> /hello로 get요청 보내기 
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }
}

```

### 2) 개념정리
```
컨테이너 ex)톰켓

- 서브릿을 관리(서블릿에 대한 요청을 받고) 답변을 주는 중간 역할을 해주는 녀석
- 클라이언트와 서블릿간의 요청과 답변을 전달해줌

클라이언트 -> 웹서버 장비
요청(get)함 ->  웹서버 -> Get -> 컨테이너(자바코드로 이루어짐) -> get(요청) -> 서블릿(자바코드)

클라이언트  <- 웹서버 장비
응답받음  <-  웹서버 <- 응답문서 -> 컨테이너<- 응답문서 전달 <- 서블릿(자바코드)

빈
(잘 모르겠음)
스프링 컨테이너가 생성해준 자바 객체를 빈이라고 한다 

Autowired
- Bean 자동주입


  ```