# week 1 

## chap 1 인텔리제이 설치 
* 설치 부분이므로 간단하게 넘어가겠습니다. (에러 같은 건 없었고 java 17 버전 사용했습니다.)

---

## chap 2 스프링 부트에서 테스트 코드를 작성하기

### 테스트 코드 
* TDD != `단위 테스트(Unit Test)`
    * TDD: 테스트가 주도하는 개발로, 테스트 코드 먼저 작성하는 것부터 시작함 
    * 단위테스트: TDD의 첫번째 단계인 기능 단위의 테스트 코드를 작성하는 것 (순수하게 테스트 코드만 작성하는 것을 의미) -> 책에서는 단위 테스트 코드에 대해 배움  
* 단위 테스트 코드 장점 
    1. 빠른 피드백 
    2. System.out.println()을 통해 눈으로 검증해야하는 문제 발생시 -> 자동검증 가능! (수동검증 필요x)
    3. 개발자가 만든 기능을 안전하게 보호해줌 
* 테스트 코드 작성을 도와주는 프레임워크: Java에서는 `JUnit`

### Hello Controller 테스트 코드 작성 
* Application 클래스는 앞으로 만들 프로젝트의 **메인 클래스**가 됨 
* `@SpringBootApplication`
    * 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성을 자동으로 설정 
    * **항상 프로젝트 최상단에 위치**해야함 (이 위치부터 설정을 읽어나가기 때문)
* `HelloController`
    * **@RestController@** : 컨트롤러를 JSON을 반환하는 컨트롤러로 만들어줌.
    * **@GetMapping** : //HTTP Method인 Get의 요청을 받을 수 있는 API를 만들어줌.
* `HelloControllerTest`
    * **@RunWith** : 테스트 진행시 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킴. 스프링 부트 테스트와 JUnit 사이 연결자 역할
    * **@WebMvcTest** : 여러 스프링 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션. 선언할 경우 @Controller, @ControllerAdvice 등 사용 가능 (여기서는 컨트롤러만 사용하기에 선언)
    * **@Autowired** : 스프링이 관리하는 bean을 주입받음.
    * **@private MockMvc mvc** : 웹 API를 테스트할 때 사용. 스프링 MVC 테스트의 시작점.
    * **@mvc.perform(get("/hello"))** : MockMvc를 통해 /hello 주소로 HTTP GET 요청을 함.
    * **@.andExpect(status().isOk())** : mvc.perform의 결과, HTTP Header의 Status를 검증함. (202,404,500의 상태 검증; 여기선 OK, 즉 200인지 아닌지를 검증)
    * **@.andExpect(content().string(hello))** : mvc.perform의 결과를 검증. 응답 본문 내용 검증. Controller에서 "hello"를 리턴하기 위해 이 값이 맞는지 검증.
* Run hello가_리턴된다() 실행 시, 성공적으로 테스트가 통과하는 것을 확인 가능했다! 

### 롬복(lombok)
* 롬복은 자바 개발할 때 많이 사용하는 Getter, Setter, 기본생성자, toString 등을 어노테이션으로 자동생성해줌. 
* `implementation('org.projectlombok:lombok')` 을 build.gradle에 등록 
* HelloController 코드를 롬복으로 전환 : *김진영/springboot-aws/src/main/java/example/org/web/dto/HelloResponseDto.java* 참고  
    ```java
    //HelloResponseDtoTest.java
    package example.org.web.dto;

    import org.junit.Test;

    import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

    public class HelloResponseDtoTest {
        @Test
        public void lombok_test(){
            //given
            String name="test";
            int amount=1000;

            //when
            HelloResponseDto dto = new HelloResponseDto(name,amount);

            //then
            assertThat(dto.getName()).isEqualTo(name);
            //assertThat(): assertj라는 테스트 검증 라이브러리의 검증 메소드. 검증하고 싶은 대상을 인자로 받음.
            //isEqualTo(): assertj의 동등 비교 메소드. assertThat에 있는 값과 isEqualTo의 값을 비교해서 같을 때만 성공.
            assertThat(dto.getAmount()).isEqualTo(amount);
        }
    }
    ```

    ```java
    //테스트 코드 in HelloControllerTest.java 
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
    ```
## 1주차 과제 
HelloController에 Get (”/hello/assignment”)을 작동시키면 
“first-assignment”라는 String 값이 return 되도록 작성해주세요
* [HelloController.java부분](./springboot-aws/src/main/java/example/org/web/HelloController.java)
    ```java
    //HelloController.java에 다음과 같은 코드 추가 
    @GetMapping("/hello/assignment")
        public String assign(){
            return "first-assignment";
        }
    ```
* [HelloControllerTest.java부분](./springboot-aws/src/test/java/example/org/web/HelloControllerTest.java)
    ```java
    //테스트를 위해 HelloControllerTest.java에 다음 코드 추가
    @Test
        public void assignment가_리턴된다() throws Exception{
            String assign="first-assignment";

            mvc.perform(get("/hello/assignment"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(assign));
        }
    ```
---

*과제 제출 기한이 내일까지인 줄 알고 느긋하게 했더니 데드라인에 아슬아슬하게 제출하네요,, 다음부턴 제대로 확인하겠습니다!!!*
