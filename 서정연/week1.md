## [1] 테스트 코드

### 1. 테스트 코드 소개
   - TDD : 테스트가 주도하는 개발
      - 테스트 코드 작성부터 시작
      - 레드 그린 사이클 : 항상 실패하는 테스트 먼저 작성(Red) -> 테스트가 통과하는 프로덕션 코드 작성(Green) -> 테스크가 통과하면 프로덕션 코드 리팩토링(Refactor)
   - 단위 테스트 : TDD의 첫 단계인 기능 단위의 테스트 코드 작성을 말함
      - 테스트 코드를 꼭 먼저 작성 X
      - 리팩토링 포함 X
      - 순수하게 테스트 코드만 작성
### 2. 테스트 코드는 왜 작성해야 할까?
   1. 빠른 피드백
      - 테스트 코드를 작성하면 빠르게 수정된 기능을 테스트해볼 수 있다
   2. 자동검증 가능
      - 단위 테스트를 실행만 하면 더는 수동검증(System.out.println()) 필요 X
   3. 개발자가 만든 기능 안전하게 보호
      - 하나의 기능 추가 할 때마다 기존 기능에 문제가 생기는 일이 빈번하게 발생
      - 새로운 기능 추가 시 기존 기능이 잘 작동되는 것을 보장
      - 기존 기능에 여러 경우를 테스트 코드로 구현해놓았다면 테스트 코그를 수행만 하면 문제를 조기에 찾을 수 있음

### 3. 테스트 코드 작성을 도와주는 프레임워크
   - xUnit 프레임워크 : JUnit-Java / DBUnit-DB / CppUnit-C++ / NUnit-.net

<br><br>
## [2] Hello Controller 테스트 코드 작성하기

   ### 1-1. Application 클래스 생성
   - Java > Package > 클래스 생성
   - 일반적으로 패키지명은 웹 사이트 주소의 역순

   ### 1-2. 클래스 코드 작성 : Application 클래스
   - import 단축키 : Option + Enter
   - 내장 WAS : 외부에 별도로 WAS를 두지 않고 애플리케이션 내부에서 실행, 서버에 톰캣을 설치할 필요 X, 스프링 부트로 만들어진 Jar파일로 실행하면 됨
   - 코드 설명
      - Application 클래스 : 프로젝트의 메인 클래스
      - @SpringBootApplication : 스프링 부트 자동 설정, 스프링 Bean 읽기와 생성 모두 자동 설정
        - 어노테이션이 있는 위치부터 설정을 읽어가기 때문에 항상 프로젝트 최상단에 위치애야 함
      - main 메소드
        - SpringApplication.run : 내장 WAS(Web Application Server) 실행
        - 스프링 부트에서는 내장 WAS 사용 권장 : 언제 어디서나 같은 환경에서 스프링 부트 배포 가능하기 때문

   ### 2-1. Controller 클래스 생성
   - Java > Package > web 패키지 생성 > 클래스 생성
   - web 패키지 : 컨트롤러와 관련된 클래스들 모아두기

   ### 2-2. 클래스 코드 작성 : HelloController 클래스
   - 코드 설명
      - @RestController : JSON을 반환하는 컨트롤러로 만들어 줌
      - @GetMapping : HTTP의 Get의 요청을 받을 수 있는 API 만들어줌
      - /hello로 요청이 오면 문자열 hello를 반환하는 기능

   ### 3-1. 테스트 코드 생성
   - src/test/java 아래 패키지(앞과 동일하게) 생성
   - 테스트 클래스 생성 : HelloControllerTest
   - 일반적으로 대상 클래스 이름 + Test

   ### 3-2. 클래스 코드 작성 : HelloControllerTest 클래스
   - 코드 설명
     - @RunWith(SpringRunner.class)
       - 테스트 진행 시 JUnit에 내장된 실행자 외의 다른 실행자 실행시킴
       - SpringRunner 스프링 실행자 사용
       - 스프링 부트 테스트와 JUnit 사이에 연결자 역할 함
     - @WebMvcTest - Web(Spring MVC)에 집중 - @Controller, @ControllerAdvice 등 사용
       -@Autowired - 스프링이 관리하는 빈을 주입받음
     - private MockMvc mvc
       - 웹 API 테스트 시 사용
       - 스프링 MVC 테스트의 시작점
       - HTTP GET, POST 등에 대한 API 테스트 가능
     - mvc.perform(get("/hello"))
       - MockMvc통해 /hello 주소로 HTTP GET 요청
       - 여러 검증 기능 이어서 선언 가능(체이닝 지원)
      - .andExpect(status().isOk())
        - mvc.perform의 결과 겸증
        - HTTP Header의 Status 검증
        - 200, 404, 500 등의 상태 검증
        - isOK() : 200 여부 검증
      - .andExpect(content().string(hello))
        - mvc.perform의 결과 겸증
        - 응답 본문의 내용 검증
        - "hello"를 리턴하는지 검증

   ### 4. 테스트 코드 실행
   - .andExpect(status().isOk())와 .andExpect(content().string(hello))가 모두 테스트를 통과했음을 의미
   - 그럼에도 의심이 된다면 수동으로도 실행 -> Application.java 정상 실행

<br><br>
## [3] 롬복 소개 및 설치하기

   ## 1. 롬복?
   - 자바 개발 시 자주 사용하는 코드 Getter, Setter, 기본생성자, toString 등을 어노테이션으로 자동 생성해줌
   - 플러그인으로 쉽게 설정 가능

   ## 2. 롬복 설치
   - 롬복 라이브러리 내려받기
     - buld.gradle dependencies에 코드 추가 : implementation 'org.projectlombok:lombok'
     - refresh해서 라이브러리(혹은 의존성) 받기
   - 롬복 플러그인 설치
     - Actions(Command + Shift + A) > Plugins >MarketPlace > lombok 설치
     - 플러그인 설치 후에는 항상 재시작
     - Settings(Command + ,) > Build > Compliler > Annotation Processors에서 Enable annotation processing 체크

<br><br>
## [4] Hello Controller 코드를 롬복으로 리팩토링하기

   ## 1. dto 패키지 추가
   - 모든 응답 Dto는 이 패키지에 추가
   
   ## 2. 클래스 생성 : HelloResponseDto 클래스
   - 코드 설명
     - @Getter -선언된 모든 필드의 get 메소드 생성해줌
     - @RequiredArgsConstructor
       - 선언된 모든 final 필드가 포함된 생성자 생성해줌
       - final 없는 필드는 포함 X
      
   ## 3-1. 롬복 테스트 코드 생성
   - src/test/java 아래 패키지(앞과 동일하게) 생성
   - 클래스 생성 : HelloResponseDtoTest
   
   ## 3-2. 클래스 코드 작성 : HelloResponseDtoTest 클래스 
   - 코드 설명 
      - assertThat
         - assertj 테스트 검증 라이브러리의 검증 메소드 
         - 검증하고 싶은 대상을 인자로 받음
         - isEqualTo와 같은 메소드 이어 사용 가능(메소드 체이닝 지원)<br>
         &#42; JUnit 기본 assertThat 아님 주의
       - isEqualTo
         - assertj의 동등 비교 메소드
         - assertThat의 값과 isEqualTo의 값을 비교하여 같다면 성공 
  
   => 에러 발생 : `error: variable name not initialized in the default constructor
   private final String name;
   ^`<br>
   -> gradle 4로 다운그레이드 하여 롬복의 @Gettter로 get 메소드 생성, @RequiredArgsConstructor로 생성자 자동 생성 확인! <br><br>
   
   &#42; (참고) Gradle 버전 다운그레이드
   1. F12로 터미널을 열어준다.
   2. `./gradlew wrapper --gradle-version 4.10.2` 입력
   3. gradle/wrapper/gradle-wrapper.properties 파일에서 버전을 확인

   ## 4. HelloController 롬복으로 리팩토링
   ## 4-1. HelloController에도 새로 만든 ResponseDto 사용하도록 코드 추가
   - 코드 설명
     - @RequestParam
       - 외부에서 API로 엄김 파라미터를 가져옴
       - "name"이란 이름으로 엄긴 파라미터를 메소드 파라미터 name에 저장
       
   ## 4-2. 추가된 API 테스트 코드 HelloControllerTest에 추가
   - 코드 설명
     - param
       - API 테스트 시 사용될 요청 파라미터 설정
       - 값은 String만 허용(String 타입이 아닌 경우 문자열로 변경해야 함)
     - jsonPath
       - JSON 응답값을 필드별로 검증하는 메소드
       - &#38;.필드명 : &#38;.name, &#38;.amount
   - JSON이 리턴되는 API 정상적으로 테스트 통과
