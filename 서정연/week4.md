## Chapter 5 : 스프링 시큐리티와 OAuth 2.0으로 로그인 기능 구현하기

- 스프링 시큐리티 : 막강한 인증과 인가(권한 부여) 기능을 가진 프레임워크
- 스프링 기반의 애플리케이션에서는 보안을 위한 표준
- 확장성 고려한 프레임워크 -> 다양한 요구사항 손쉽게 추가 및 변경 가능

### [1] 스프링 시큐리티와 스프맅 시큐리티 Oauth2 클라이언트

- 많은 서비스에서 id와 pw 방식 < 소셜 로그인 기능 사용(구글, 페이스북, 네이버)
- 직접 구현 시 로그인 시 보안, 비밀번호 찾기/변경, 회원가입 시 이메일 혹은 전화번호 인증, 회원전보 변경 등 구현해야 함
- OAuth 로그인 구현 시 위 목록의 것들을 모두 구글, 페이스북, 네이버 등에 맡기면 되기에 서비스 개발 집중 가능
- 스프링 부트 1.5 vs 스프링 부트 2.0
  - `spring-swcurity-oauth2-autoconfigure` 라이브러리 사용
  - 스프링 부트 2에서도 기존 1.5의 설정 그대로 사용 가능
  - 이 책에서는 Spring Security Oauth2 Client 라이브러리 사용!(스프링 2 방식)
  - client 인증 정보만 입력하면 됨
  - 1.5에서 직접 입력했던 값들 -> enum으로 대체

### [2] 구글 서비스 등록

1. 구글 서비스에서 신규 서비스 생성
   - 발급된 인증 정보(clientId + clientSecret)로 로그인 기능과 소셜 서비스 기능 사용
   1. 구글 클라우드 플랫폼 이동(https://console.cloud.google.com/welcome?project=durable-rhythm-348606)
   - 프로젝트 선택 > 새 프로젝트 > 등록될 서비스 이름 입력 > 생성
   - 왼쪽 메뉴 탭 > API 및 서비스 > 사용자 인증 정보 > 사용자 인증 정보 만들기 > OAuth 클라이언트 ID 선택
   - 생성 전 동의 화면 구성
   - OAuth 클라이언트 ID 만들기
     - 승인된 리디렉션 URI : 인증 성공시 구글에서 리다이렉트할 URL
     - 스프링부트2의 시큐리티에서는 기본적으로 {도메인}/login/oauth2/code/{소셜서비스코드}로 리다이렉트 URL 지원
     - 현재는 개발 단계이므로 도메인은 localhost:8080
     - AWS 서버 배포 시 localhost외에 추가로 주소 추가해야 함
   - OAuth 클라이언트 ID 생성
   2. 프로젝트 설정
   - src/main/resources 아래 application-oauth.properties 파일 생성
   - 클라이언트 ID 및 클라이언트 보안 비밀 코드 등록
   - `scope = profile, email` : 기본값이 openid, profile, email
   - Open Id Provider로 인식하게 되고 이 경우 Openid Provider인 서비스(구글)과 그렇지 않은 서비스로 나눠서 각각 만들어야 함 -> 하나의 OAuth2Service로 사용하기 위해 openid scope 빼고 등록
   - 스프링 부트에서 properties 이름을 aplication-xxx.preperties로 만들면 xxx이름의 profile 생성되어 이를 통해 관리 가능
   - profile = xxx 호출 시 해당 properties의 설정 가져올 수 있음
   - 호출하는 방식 : application.properties애서 application-oauth.properties 포함하도록 구성
   - spring.profiles.include=oauth 코드 추가
   3. .gitignore 등록
   - 클라이언트 ID 및 클라이언트 보안 비밀은 보안 중요한 정보들 -> 외부 노출 X
   - .gitignore에 application-oauth.properties 파일 추가

### [3] 구글 로그인 연동하기

1. 사용자 정보 담당 도메인 User 클래스 생성

- domain/user 아래 User 클래스 생성
- 주요 코드
  - `@Enumerated(EnumType.STRING)`
    - Enum값을 어떤 형태로(String) 저장할지 결정
    - 기본은 int 숫자로 저장 -> 그 값이 무슨 코드를 의미하는 지 알 수 X
    - 문자열로 저장될 수 있도록 선언

2. 각 사용자 권한 관리할 Enum 클래스 Role 생성

- domain/user 아래 Role 클래스 생성
- 주요 코드
  - `GUEST("ROLE_GUEST", "손님")`
    - 스프링 시큐리티에서는 권한 코드에 항상 ROLE\_이 앞에 있어야 함 -> 코드별 키 값을 ROLE_xxx으로 저장

3. User의 CRUD 위한 UserRepository 생성

- domain/user 아래 UserRepository 클래스 생성
- 주요 코드
  - `Optional<User> findByEmail(String email);`
    - findByEmail : 소셜 로그인 시 반환되는 값 중 email로 사용자가 회원인지 아닌지 판단

4. 스프링 시큐리티 설정

- build.gradle에 스프링 시큐리티 관련 의존성 추가
  - `spring-boot-starter-oauth2-client` : 클라이언트 입장(소셜 로그인)에서의 소셜 기능 구현 시 필요한 의존성
- OAuth 라이브러리 이용한 소셜 로그인 설정 코드 작성
  - config.auth 패키지 생성 <- 시큐리티 관련 클래스들
  - SecurityConfig 클래스 생성 : 설정 코드 작성
    - 주요 코드
      - `@EnableWebSecurity` : Spring Security 설정 활성화
      - `.csrf().disable().headers().frameOptions().disable()` : h2-console 화면 사용 위해 해당 옵션들 disable
      - `authorizeRequests()`
        - URL별 권한 관리 설정하는 옵션의 시작점
        - 선언 후에만 antMatchers 옵션 사용 가능
      - `antMatchers`
        - 권한 관리 대상 지정
        - URL, HTTP 메소드별로 관리 가능
        - 지정된 URL들은 permitAll()옵션으로 전체 열람 권한 부여
        - "/api/v1/\*\*" 주소 가진 API는 USER권한 가진 사람만 가능하도록 설정(`hasRole(Role.USER.name())`)
        - `anyRequest()`
          - 설정된 값들 이외의 URL들
          - authenticated() 추가하여 나머지 URL들은 모두 로그인한 사용자들에게만 허용
        - `logout().logoutSuccessUrl("/")`
          - 로그아웃 기능에 대한 여러 설정의 시작점
          - 로그아웃 성공 시 / 주소로 이동
        - `oauth2Login()` : 로그인 기능에 대한 여러 설정의 시작점
        - `userInfoEndpoint()` : 로그인 성공 후 사용자 정보 가져올 때의 설정들 담당
        - `userService`
          - 로그인 성공 시 후속 조치 진행할 UserService 인터페이스 구현체 등록
          - 사용자 정보 자져온 상태에서 추가로 진행하고자 하는 기능 명시
  - CustomOAuth2UserService 클래스 생성
    - 로그인 이후 가져온 사용자 정보 기반 -> 가입 및 정보 수정, 세션 저장 등의 기능 지원
    - 주요 코드
      - `registrationId` : 현재 로그인 진행 중인 서비스 구분 코드
      - `userNameAttributeName`
        - 키가 되는 필드값(Primary Key)
        - 구글은 기본적으로 코드 지원 "sub"
      - `OAuthAttributes` : OAuth2UserService 통해 가져온 OAuth2User의 attribute 담는 클래스
      - `SessionUser` : 세션에 사용자 정보 저장하기 위한 Dto 클래스
  - Dto 클래스 생성
    - OAuthAttributes 클래스 생성
      - config.auth.dto 패키지 아래 생성
      - 주요 코드
        - `of()` : OAuth2User에서 반환하는 사용자 정보 하나하나 변환
        - `toEntity()` - User 엔티티 생성(생성 시점은 처음 가입 할 때)
        - 가입 시 기본 권한 주기 위해 role 빌더값으로 `.role(Role.GUEST)`사용
    - SessionUser 클래스 생성
      - 인증된 사용자 정보만 필요 -> name, email, picture만 필드로 선언
        <br><br>

=> User 클래스 사용 X 이유

- User 클래스 직렬화 구현하지 않았다는 에러 발생
- User 클래스는 엔티티이기 때문에 언제 다른 엔티티와 관계 형성될지 모름
- 에러 해결을 위해 직렬화 코드 추가하면 직렬화 대상에 자식들까지 포함 -> 성능이슈, 부수효과 발생<br>
  -> 직렬화 기능 가진 세션 Dto 추가로 생성!

5. 로그인 테스트

- index.mustache에 로그인 버튼과 로그인 성공 시 사용자 이름 보여주도록 코드 추가
  - 코드 설명
    - `{{#userName}}` : userName이 있다면 노출시키도록
    - `a href="/logout"` : 시큐리티에서 기본 제공하는 로그아웃 URL
    - `{{^userName}}` : 해당 값 존재하지 X 경우 -> 로그인 버튼 노출
    - `a href="/oauth2/authorization/google"` : 시큐리티에서 기본 제공하는 로그인 URL
- userName 사용 가능하도록 IndexController에서 userName을 model에 저장하도록 코드 추가
  - 코드 설명
    - `(SessionUser) httpSession.getAttribute("user")`
      - 로그인 성공 시 세션에 SessionUser 저장하도록 구성
      - 로그인 성공 시 httpSession.getAttribute("user")에서 값 가져올 수 있음
    - `if(user != null)` : 세션에 저장된 값 있을 때만 model에 userName 등록

### [4] 어노테이션 기반으로 개선하기

- IndexController에서 세션값 가져오는 부분 개선

  - 다른 컨트롤러와 메소드에서 세션값 필요 -> 직접 매번 가져와야 함
  - 메소드 인자로 세션값을 바로 받을 수 있도록 변경

  1. @LoginUser 어노테이션 생성

  - 코드 설명
    - `@Target(ElementType.PARAMETER)`
    - 어노테이션 생성 위치 지정
    - PARAMETER : 메소드의 파라미터로 선언된 객체에서만 사용 가능
    - `@interface` : 이 파일을 어노테이션 클래스로 지정

  2. LoginUserArgumentResolver 클래스 생성

  - HandlerMethodArgumentResolver 인터페이스 구현한 클래스
  - 조건에 맞는 경우 메소드가 있다면 HandlerMethodArgumentResolver의 구현체가 지정한 값으로 해당 메소드의 파라키터로 엄길 수 있음
  - 코드 설명
    - `supportsParameter()`
      - 컨트롤러 메소드의 특정 파라미터 지원하는지 판단
      - 파라미터에 @LoginUser 어노테이션 있고, 파라미터 클래스 타입이 SessionUser.class인 경우 true
    - `resolveArgument()` : 파라미터에 전달할 객체 생성

  3. LoginUserArgumentResolver가 스프링에서 인식될 수 있도록 WebMvcConfigurer에 추가

  - config 패키지 아래 Webconfig 클래스 생성
  - HandlerMethodArgumentResolver는 항상 WebMvcConfigurer의 addArgumentResolvers() 통해 추가

  4. IndexController에서 반복되는 코드 모두 @LoginUser로 개선

  - 코드 설명
    - `@LoginUser SessionUser user`
    - 기존에 `(User) httpSession.getAttribute("user")`로 가져오던 세션 정보 값 개선
    - 어느 컨트롤러든지 @Loginuser만 사용하면 세션정보 가져올 수 있음

### [5] 세션 저장소로 데이터베이스 사용하기

- 추가 개선점 1 : 현재 애플리케이션은 재실행 시 로그인 풀림
  - 내장 톰캣의 메모리에 저장되기 때문(WAS의 메모리)
  - 애플리케이션 실행 시 실행되는 구조에서는 항상 초기화
- 추가 개선점 2 : 2대 이상의 서버에서 서비스하고 있가면 톰캣마다 세션 동기화 설정 필요
- 실제 협업에서는 어떻게 해결?
  - 톰캣 세션 사용
  - MySQL 같은 데이터베이스를 세션 저장소로 사용
  - Redis, Memcached 같은 메모리 DB를 세션 저장소로 사용
- 이 책에서는 2번째 방식 선택

1. buid.gradle에 spring-session-jdbc 의존성 등록
2. applications.properties에 세션 저장소로 jdbc 선택하도록 코드 추가

- h2-console에 세션을 위한 테이블 2개 생성 확인 -> JPA로 인해 세션 테이블이 자동 생성됨

### [6] 네이버 로그인

1. 네이버 API 등록

- 네이버 오픈 API로 이동()

2. ClientID 및 ClientSecret application-oauth.properties에 등록

- 네이버에서는 스프링 시큐리티 공식 지원 X -> 전부 수동으로 입력
- 코드 설명
  - `user-name-attribute=response` : 기준이 되는 user_name의 이름을 네이버에서는 response로 해야 함(네이버의 회원 조회 시 반환되는 JSON 형태 때문)
  - 이후 자바 코드로 response의 id를 user_name으로 지정

3. 스프인 시큐리티 설정 등록

- OAuthAttributed에 네이버인지 판단하는 코드와 네이버 생성자 추가

4. index.mustache에 네이버 로그인 버튼 추가

### [7] 기존 테스트에 시큐리티 적용하기

- 기존 테스트에 시큐리티 적용으로 문제가 되는 부분들 해결
  - 인증된 사용자만 API호출 가능
  - 기존의 API 테스트 코드들이 모두 인증 권한 받지 X -> 테스트 코드마다 인증한 사용자가 호출한 것처엄 작동하도록 수정

1. Gradle 탭 > Tasks > verfication > test 전체 테스트 수행

- 롬복 이용한 테스트 외에 스프링 이용한 테스트 모두 실패<br>

문제 1 : CustomOAuth2UserService 찾지 X

- CustomOAuth2UserService 생성하는데 필요한 소셜 로그인 관련 설정값들이 없기 때문에 발생
- 분명 application-oauth.properties에 설정값들 추가했는데?
  - src/main과 src/test의 환경 차이 때문
  - test에 application.properties 없으면 main의 설정 그대로 가져옴 but 그 범위는 application.properties 파일까지(application-oauth.properties는 가져오는 파일 X)<br>

문제 1 해결 : 테스트 환경을 위한 application.properties 생성

- test/resources 아래 application.properties 생성

문제 2 : 302 Status Code

- 에러 발생 : `expected:<[200 OK]> but was:<[302 FOUND]>`
- 200(정상응답)을 원했지만 -> 302(리다이렉션 응답)이 옴
- 스프링 시큐리티 인증 때문에 인증되지 않은 사용자의 요청 이동시키기 때문<br>

문제 2 해결 : 임의로 인증된 사용자 추가하여 API 테스트

- build.gradle에 하는 spring-security-test 추가
- PostsApiControllerTest의 2개 테스트 메소드에 임의의 사용자 인증 추가
  - `@WithMockUser(roles = "USER")`
    - 모의 사용자 만들어 사용
    - roles에 권한 추가
    - ROLE_USER 권한 가진 사용자가 API 요청하는 것과 동일한 효과
- @WithMockUser는 MockMvc에서만 작동 -> @SpringBootTest에서 MockMvc 사용하도록 코드 추가
  - `@Before` : 매번 테스트 시작 전 MockMvc 인스턴스 생성
  - `mvc.perform` : 생성된 MockMvc로 API 테스트 진행

문제 3 : @WebMvcTest에서 CustomOAuth2UserService 찾지 X

- 문제 1번 해결을 통해 스프링 시큐리티 설정을 잘 작동 but @WebMvcTest는 CustomOAuth2UserService 스캔 X
- @Repository, @Service, @Component는 스캔 대상 아님

문제 3 해결 : 스캔 대상에서 SecurityConfig 제거

- 추가 에러 발생 : @EnaleJPAAuditing 때문
  - @EnaleJPAAuditing 사용 -> 최소 하나의 @Entity 클래스 필요 but @WebMvcTest이다 보니 당연히 없음
  - @EnaleJPAAuditing가 @SpringBootApplication와 함께 있다보니 @WebMvcTest에서도 스캔함 -> 둘을 분리해야 함
- 추가 에러 해결
  - Application.java에서 @EnaleJPAAuditing 제거
  - config 패기지 아래 JpaConfig 생성하여 @EnaleJPAAuditing 추가
