# WEEK 4

## CHAPTER 05: 스프링 시큐리티와 OAuth 2.0으로 로그인 기능 구현하기

스프링 시큐리티(Spring Security) 

- 막강한 인증(Authentication)과 인가(Authorization)(혹은 권한 부여)기능을 가진 프레임 워크
- 사실상 스프링 기반의 애플리케이션에서는 보안을 위한 표준
- 인터셉터, 필터 기반의 보안 기능을 구현하는 것보다 스프링 시큐리티를 통해 구현하는 것을 적극 권장

---

### 5.1 스프링 시큐리티와 스프링 시큐리티 Oauth2 클라이언트

- 소셜로그인 하는 이유
    
    구현해야할 것이 매우 많아짐..(로그인 시 보안, 비밀번호 찾기, 전화번호 인증, 비밀번호 변경, 회원정보 변경 등등)
    
- OAuth 로그인 구현 시 모두 구글, 네이버 등에 맡기면 됨!
- 스프링 부트 1.5 vs 스프링 부트 2.0
    - 스프링 부트 1.5에서의 OAuth2 연동 방법이 2.0에서는 크게 변경됨
    - 하지만 설정방법에 크게 차이가 없음
    - `spring-security-oauth2=autoconfigure` 라이브러리 덕분
        - 1.5에서 쓰던 설정을 그대로 사용 가능
    - 하지만 이 책에서는 스프링 부트 2 방식인 Spring Security Oauth2 Client 라이브러리 사용
        - 스프링 팀에서 spring-security-oauth 프로젝트는 유지상태로 결정, 신규 기능 추가하지 않고 버그 수정 정도만. 신규 기능은 oauth2 라이브러리에서만 지원
        - 스프링 부트용 라이브러리(starter) 출시
        - 기존에 사용되던 방식은 확장 포인트가 적절하게 오픈되어 있지 않아 직접 상속하거나 오버라이딩 해야하고 신규 라이브러리의 경우 확장 포인트를 고려하여 설계된 상태
    - 스프링 부트 2 방식의 자료를 찾고 싶은 경우
        - spring-security-oauth2-autoconfigure 라이브러리를 썼는지 확인
        - application.properties 혹은 application.yml정보 확인
        - 스프링 부트 1.5 방식은 url 주소를 모두 명시해야하지만 2.0 방식에서는 client인증 정보만 입력하면 됨
        - 1.5 버전에서 모두 입력하던 값들은 2.0 버전에서 모두 enum

---

### 5.2 구글 서비스 등록

- 구글 서비스에 신규 서비스 등록
    - 발급된 인증 정보(clientId와 clientSecret)를 통해 로그인 기능과 소셜 서비스 기능 사용
    1. 구글 클라우드 플랫폼 → 새 프로젝트 → 서비스 이름 등록
    2. 왼쪽 메뉴 → API 및 서비스 → 사용자 인증정보 → 사용자 인증 정보 만들기 → OAuth 클라이언트 ID → 어플리케이션 유형(웹 어플리케이션), 이름 설정 → 승인된 리디렉션 : http://localhost:8080/login/oauth2/code/google
    3. 동의 화면 구성 → 어플리케이션 이름 작성
- 승인된 리디렉션 URI
    - 서비스에서 파라미터로 인증 정보를 주었을 때 인증이 성공하면 구글에서 리다이렉트할 URL
    - 스프링 부트 2 버전의 시큐리티에서는 기본적으로 {도메인}/login/oauth2/code/{소셜서비스코드}로 리다이렉트 URL 지원
    - 사용자가 별도로 리다이렉트 URL을 지원하는 Controller를 만들 필요 없음
    - AWS 서버에 배포하게 되면 localhost 외에 추가로 주소를 추가해야 됨.
- application-oauth 등록
    - application-properties가 있는 src/main/resources/ 디렉토리에 application-oauth.properties 파일 생성
    
    ```java
    spring.security.oauth2.client.registration.google.client-id=클라이언트 ID
    spring.security.oauth2.client.registration.google.client-secret=클라이언트 보안 비밀
    spring.security.oauth2.client.registration.google.scope=profile,email
    ```
    
    - scope=profile,email
        - 많은 예제에서는 이 scope를 별도로 등록하지 않음
        - 기본값이 openid, profile, email이기 때문
        - 강제로 profile, email를 등록한 이유는 openid라는 scope가 있으면 Open Id Provider로 인식하기 때문
        - 이렇게 되면 Open Id Provider인 서비스(구글)와 그렇지 않은 서비스(네이버/카카오 등)로 나눠서 각각 OAuth2Service를 만들어야 함
        - 하나의 OAuth2Service로 사용하기 위해 일부러 openid scope를 빼고 등록
    - properties의 이름을 application-xxx.properties로 만들면 xxx라는 이름의 profile이 생성되어 이를 통해 관리 가능 → applicaiton.properties에서 application-oauth.properties를 포함하도록 구성
        
        ```java
        spring.profiles.include=oauth
        ```
        
- .gitignore등록
    - 구글 로그인을 위한 클라이언트 ID와 클라이언트 보안 비밀은 보안이 중요한 정보들
    - 깃허브에 올라가면 개인정보를 가져갈 수 있는 취약점이 됨
    
    ```java
    /src/main/resources/application-oauth.properties
    ```
    

---

### 5.3 구글 로그인 연동

1. domain/user/User
    - @Enumerated(EnumType.STRING)
        - JPA로 데이터베이스를 저장할 때 Enum값을 어떤 형태로 저장할지를 결정
        - 기본적으로는 int로 된 숫자가 저장됨
        - 숫자로 저장되면 데이터베이스로 확인할 때 그 값이 무슨 코드를 의미하는지 알 수가 없음
        - 그래서 문자열 (EnumType.STRING)로 저장될 수 있도록 선언
    
2. Enum 클래스 Role 생성
    - 각 사용자의 권한 관리
    - 스프링 시큐리티에서는 권한 코드에 항상 ROLE_이 앞에 있어야함
    
    ```java
    @Getter
    @RequiredArgsConstructor
    public enum Role {
        GUEST("ROLE_GUEST","손님"),
        USER("ROLE_USER","일반 사용자");
        
        private final String key;
        private final String title;
    }
    ```
    
3. UserRepository 생성
    - findByEmail
        - 소셜 로그인으로 반환되는 값 중 email을 통해 이미 생성된 사용자인지 처음 가입하는 사용자인지를 판단하기 위한 메소드
    
    ```java
    public interface UserRepository extends JpaRepository<User,Long> {
        Optional<User> findByEmail(String email);
    }
    ```
    

**스프링 시큐리티 설정**

1. build.gradle에 스프링 시큐리티 관련 의존성 추가
    - spring-boot-starter-oauth2-client
        - 소셜 로그인 등 클라이언트 입장에서 소셜 기능 구현 시 필요한 의존성
        - spring-boot-starter-oauth2-client와 spring-boot-starter-oauth2-jose를 기본으로 관리해줌
    
    ```java
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
    ```
    
2. config.auth 패키지 생성
3. SecurityConfig
    - @EnableWebSecurity
        - Spring Security 설정들을 활성화 시켜줌
    - .csrf().disable().headers().frameOptions().disable()
        - h2-console 화면을 사용하기 위해 해당 옵션들을 disable
    - authorizeRequests
        - URL별 권한 관리를 설정하는 옵션의 시작점
        - authorizeRequests가 선언되어야지만 antMatchers 옵션 사용 가능
    - antMatchers
        - 권한 관리 대상을 지정하는 옵션
        - URL, HTTP 메소드별로 관리 가능
        - “/”등 지정된 URL들은 permitAll()옵션을 통해 전체 열람 권한을 줌
        - "/api/v1/**"주소를 가진 API는 USER권한을 가진 사람만 가능하도록
    - anyRequest
        - 설정된 값들 이외 나머지 URL
        - 여기서는 authenticated()을 추가하여 나머지 URL들은 모두 인증된 사용자(로그인한 사용자)에게만 허용
    - logout().logoutSuccessUrl("/")
        - 로그아웃 기능에 대한 여러 설정의 진입점
        - 로그아웃 성공 시 / 주소로 이동
    - oauth2Login
        - OAuth2 로그인 기능에 대한 여러 설정의 진입점
    - userInfoEndpoint
        - OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
    - userService
        - 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
        - 리소스 서버(소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
    - customOAuth2UserService 대신 `@Bean public SecurityFilterChain filterChain`과 return http.build()
    - antMatchers()(mvcMatchers(), regexMatchers())
        - requestMatchers() (또는 securityMatchers()) 로 변경
    
    ```java
    @RequiredArgsConstructor
    @EnableWebSecurity
    public class SecurityConfig {
    
        private final CustomOAuth2UserService customOAuth2UserService;
    
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .headers().frameOptions().disable()
                    .and()
                    .authorizeRequests()
                    .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                    .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                    .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .and()
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService);
            return http.build();
        }
    }
    ```
    
4. CustomOAuth2UserService 클래스 생성
    - registrationId
        - 현재 로그인 진행 중인 서비스를 구분하는 코드
        - 나중에 네이버 로그인 연동 시에 네이버인지 구글인지 구분
    - userNameAttributeName
        - OAuth2 로그인 진행 시 키가 되는 필드값
        - Primary Key와 같은 의미
        - 구글의 경우 기본적으로 코드를 지원하지만 네이버 카카오 등은 기본 지원하지 않음
        - 구글의 기본 코드는 “sub”
        - 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용
    - OAuthAttributes
        - OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        - 이후 네이버 등 다른 소셜 로그인도 이 클래스 사용
    - SessionUser
        - 세션에 사용자 정보를 저장하기 위한 Dto 클래스
    
5. OAuthAttributes 클래스 생성
    - of()
        - OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환해야 함
    - toEntity()
        - User 엔티티 생성
        - OAuthAttributes에서 엔티티를 생성하는 시점은 처음 가입할 때
        - 가입할 때의 기본 권한을 GUEST로 주기 위해서 role 빌더값에는 Role.GUEST를 사용
        - OAuthAttributes 클래스 생성이 끝났으면 같은 패키지에 SessionUser 클래스를 생성
    
6. config.auth.dto 패키지에 SessionUser 클래스 추가
    - 인증된 사용자 정보만 필요
    
    ```java
    @Getter
    public class SessionUser implements Serializable {
        private String name;
        private String email;
        private String picture;
    
        public SessionUser(User user) {
            this.name = user.getName();
            this.email = user.getEmail();
            this.picture = user.getPicture();
        }
    }
    ```
    

왜 User 클래스를 사용하면 안되나요?

User 클래스를 세션에 저장하려고 하면 User 클래스에 직렬화를 구현하지 않았다는 오류 발생 

→ User에 직렬화 코드를 넣으면 되는거 아닌가?

→ User 클래스는 엔티티 이기 때문에 언제 다른 엔티티와 관계가 형성될지 모른다

→ 따라서 직렬화를 하면 성능 이슈, 부수 효과가 발생활 확률이 높아짐

**로그인 테스트**

1. 화면에 로그인 버튼 추가 - index.mustache에 추가
    - {{#userName}}
        - 머스테치는 if문 제공x
        - true/false만 판단
        - 그래서 항상 최종값을 넘겨주어야 함
        - userName이 있다면 userName 노출시키도록
    - a href="/logout"
        - 스프링 시큐리티에서 기본적으로 제공하는 로그아웃 URL
        - SecurityConfig 클래스에서 URL을 변경할 순 있지만 기본 URL을 사용해도 충분
    - {{^userName}}
        - 머스테치에서 해당값이 존재하지 않는 경우에는 ^사용
        - userName이 없다면 로그인 버튼 노출
    - <a href="/oauth2/authorization/google"
        - 스프링 시큐리티에서 기본적으로 제공하는 로그인 URL
    
    ```java
    <div class="col-md-6">
                <a href="/posts/save" role="button" class="btn btn-primary">글 등록</a>
                {{#userName}}
                    Logged in as: <span id="user">{{userName}}</span>
                    <a href="/logout" class="btn btn-info active" role="button">Logout</a>
                {{/userName}}
                {{^userName}}
                    <a href="/oauth2/authorization/google" class="btn btn-success active" role="button">Google Login</a>
                {{/userName}}
            </div>
    ```
    
2. IndexController 수정
    - index.mustache에서 userName을 사용할 수 있게 userName을 model에 저장하는 코드 추가
    - SessionUser user = (SessionUser) httpSession.getAttribute("user");
        - 앞서 작성된 CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성
        - 로그인 성공 시 httpSession.getAttribute("user")에서 값 가져옴
    - if(user != null)
        - 세션에 저장된 값이 있을 때만 model에 userName으로 등록
        - 세션에 저장된 값이 없으면 model엔 아무런 값이 없는 상태이니 로그인 버튼이 보임
    
    ```java
    private final HttpSession httpSession;
    
        @GetMapping("/")
        public String index(Model model){
            model.addAttribute("posts",postsService.findAllDesc());
            SessionUser user = (SessionUser) httpSession.getAttribute("user");
            
            if(user != null){
                model.addAttribute("userName",user.getName());
            }
                    return "index";
            }
    ```
---

### 5.4 어노테이션 기반으로 개선하기

나쁜 코드 - 같은 코드가 반복 → IndexController에서 세션값을 가져오는 부분 

`SessionUser user = (SessionUser) httpSession.getAttribute(”user”);`

→ 메소드 인자로 세션값을 바로 받을 수 있도록 변경

1. @LoginUser 어노테이션 생성
    - @Target(ElementType.PARAMETER)
        - 이 어노테이션이 생성될 수 있는 위치 지정
        - PARAMETER로 지정했으니 메소드의 파라미터로 선언된 객체에서만 사용 가능
        - 이 외에도 클래스 선언문에 쓸 수 있는 TYPE 등이 있음
    - @interface
        - 이 파일을 어노테이션 클래스로 지정
        - LoginUser라는 이름을 가진 어노테이션이 생성되었다.
    
    ```Java
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LoginUser {
    }
    ```
    
2. LoginUserArgumentResolver 생성
    - supportsParameter()
        - 컨트롤러 메소드의 특정 파라미터를 지원하는지 판단
        - 파라미터에 @LoginUser 어노테이션이 붙어 있고, 파라미터 클래스 타입이 SessionUser.class인 경우 true 반환
    - resolveArgument()
        - 파라미터에 전달할 객체 생성
        - 여기서는 세션에서 객체를 가져옴
    
    ```Java
    @RequiredArgsConstructor
    @Component
    public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
        private final HttpSession httpSession;
    
        @Override
        public boolean supportsParameter(MethodParameter parameter){
            boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
            
            boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
            
            return isLoginUserAnnotation && isUserClass;
    
        }
        
        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception{
            return httpSession.getAttribute("user");
        }
    }
    ```
    
3. WebMvcConfigurer 추가
    - LoginUserArgumentResolver가 스프링에서 인식될 수 있도록
    
    ```Java
    @RequiredArgsConstructor
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        private final LoginUserArgumentResolver loginUserArgumentResolver;
        
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
            argumentResolvers.add(loginUserArgumentResolver);
        }
    }
    ```
    
4. IndexController 코드 수정
    - @LoginUser SessionUser user
        - 기존에 (User)httpSession.getAttribute(”user”)로 가져오던 세션 정보 값이 개선
        - 이제는 어느 컨트롤러든지  @LoginUser만 사용하면 세션 정보를 가져올 수 있음
    
    ```Java
    @GetMapping("/")
        public String index(Model model, @LoginUser SessionUser user){
            model.addAttribute("posts",postsService.findAllDesc());
    
            if(user != null){
                model.addAttribute("userName",user.getName());
            }
            return "index";
        }
    ```
    

---

### 5.5 세션 저장소로 데이터베이스 사용하기

지금은 애플리케이션을 재실행하면 로그인이 풀림

→ 세션이 내장 톰캣의 메모리에 저장되기 때문

→ 내장 톰캣처럼 애플리케이션 실행 시 실행되는 구조에선 항상 초기화 : 배포할 때마다 톰캣이 재시작되는것

→ 또, 2대 이상의 서버에서 서비스하고 있다면 톰캣마다 세션 동기화 설정을 해야만 함

세션 저장소 선택

1. 톰캣 세션 사용
    1. 일반적으로 별다른 설정을 하지 않을 때 기본적으로 선택되는 방식
    2. 이렇게 될 경우 톰캣(WAS)에 세션이 저장되기 때문에 2대 이상의 WAS가 구동되는 환경에서는 톰캣들 간의 세션 공유를 위한 추가 설정 필요
2. MySQL과 같은 데이터베이스를 세션 저장소로 사용
    1. 여러 WAS간의 공용 세션을 사용할 수 있는 가장 쉬운 방법
    2. 많은 설정 x, 로그인 요청마다 DB IO가 발생하여 성능상 이슈 발생
    3. 로그인 요청이 많이 없는 백오피스, 사내 시스템 용도로 사용
3. Redis, Memcached와 같은 메모리 DB 사용
    1. B2C 서비스에서 가장 많이 사용
    2. 실제 서비스로 사용하기 위해서 Embedded Redis와 같은 상식이 아닌 외부 메로리 서버 필요

우리는 두 번째 방식 사용

**spring-session-jdbc 등록**

1. build.gradle에 의존성 등록
    
    ```Java
    implementation 'org.springframework.session:spring-session-jdbc'
    ```
    
2. application.properties
    
    ```Java
    spring.session.store-type=jdbc
    ```

---

### 5.6 네이버 로그인

1. https://developers.naver.com/apps/#/register?api=nvlogin
2. 애플리케이션 이름, 사용 API → 네이버 아이디로 로그인 → 회원이름, 이메일, 프로필 사진 체크
3. PC웹 -> 서비스 URL : http://localhost:8000/, Callback URL : http://localhost:8000/login/oauth2/code/naver
4. application-oauth.properties
    - user-name-attribute=response
        - 기준이 되는 user_name의 이름을 네이버에서는 response로 해야 함
        - 이유는 네이버의 회원 조회 시 반환되는 JSON 형태
    
    ```Java
    # registration
    spring.security.oauth2.client.registration.naver.client-id=네이버클라이언트ID
    spring.security.oauth2.client.registration.naver.client-secret=네이버클라이언트시크릿
    spring.security.oauth2.client.registration.naver.redirect-uri={baseUrl}/{action}/oauth2/code/{registrationId}
    spring.security.oauth2.client.registration.naver.authorization-grant-type=authorization_code
    spring.security.oauth2.client.registration.naver.scope=name,email,profile_image
    spring.security.oauth2.client.registration.naver.client-name=Naver
    
    # provider
    spring.security.oauth2.client.provider.naver.authorization-uri=https://nid.naver.com/oauth2.0/authorize
    spring.security.oauth2.client.provider.naver.token-uri=https://nid.naver.com/oauth2.0/token
    spring.security.oauth2.client.provider.naver.user-info-uri=https://openapi.naver.com/v1/nid/me
    spring.security.oauth2.client.provider.naver.user-name-attribute=response
    ```
    

**스프링 시큐리티 설정 등록**

네이버는 쉽게 등록 가능 

1. OAuthAttributes

    - 네이버인지 판단하는 코드와 네이버 생성자 추가

    ```Java
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
            if("naver".equals(registrationId)) {
                return ofNaver("id", attributes);
            }

            return ofGoogle(userNameAttributeName, attributes);
        }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            return OAuthAttributes.builder()
                    .name((String) response.get("name"))
                    .email((String) response.get("email"))
                    .picture((String) response.get("profile_image"))
                    .attributes(response)
                    .nameAttributeKey(userNameAttributeName)
                    .build();
        }
    ```

2. index.mustache
    - 네이버 로그인 버튼 추가
    - /oauth2/authorization/naver
        - 네이버 로그인 URL은 application-oauth.properties에 등록한 redirect-uri 값에 맞춰 자동으로 등록됨
        - /oauth2/authorization/까지는 고정이고 마지막 Path만 각 소셜 로그인 코드 사용
    
    ```Java
    {{^userName}}
                    <a href="/oauth2/authorization/google" class="btn btn-success active" role="button">Google Login</a>
                    <a href="/oauth2/authorization/naver" class="btn btn-success active" role="button">Naver Login</a>
    {{/userName}}
    ```
    

---

### 5.7 기존 테스트에 시큐리티 적용하기

기존 테스트에 시큐리티 적용으로 문제가 되는 부분들을 해결해보자.

기존에는 바로 API를 호출할 수 있어 테스트 코드 역시 바로 API를 호출하도록 구성 
→ 시큐리티 옵션이 활성화되면 인증된 사용자만 API 호출 가능

→ 기존의 API 테스트 코드들이 모두 인증에 대한 권한을 받지 못하였으므로, 테스트 코드마다 인증한 사용자가 호출한 것 처럼 작동하도록 수정

gradle → Tasks → verification → test : 전체 테스트 수행

: 롬북을 이용한 테스트 외에 스프링을 이용한 테스트는 모두 실패

1. CustomOAuth2UserService을 찾을 수 없음
    1. “hello가_리턴된다” → CustomOAuth2UserService를 생성하는데 필요한 소셜 로그인 관련 설정값들이 없기 때문에 발생 → application-oauth.properties는 test파일에 없다고 가져오는 파일이 아님
    2. application.properties를 만들어 줌.(실제로 구글 연동까지 진행할 것은 아니므로 가짜 설정값)
    
    ```Java
    spring.jpa.show_sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
    spring.h2.console.enabled=true
    spring.session.store-type=jdbc
    
    # Test OAuth
    
    spring.security.oauth2.client.registration.google.client-id=test
    spring.security.oauth2.client.registration.google.client-secret=test
    spring.security.oauth2.client.registration.google.scope=profile,email
    ```
    
2. 302 status code
    1. “Posts_등록된다” → 200(정상 응답) Status Code를 원했는데 302(리다이렉션 응답) Status Code가 와서 실패
    2. 스프링 시큐리티 설정 때문에 인증되지 않은 사용자의 요청은 이동시키기 때문
    3. 임의로 인증된 사용자를 추가하여 API만 테스트해볼 수 있도록 수정
    4. spring-security-test를 build.gradle에 추가 : 스프링 시큐리티 테스트를 위한 여러도구 지원
        
        ```Java
        testCompileOnly 'org.springframework.security:spring-security-test'
        ```
        
    5. PostsApiControllerTest의 2개 테스트 메소드 수정
        - @WithMockUser(roles="USER")
            - 인증된 모의(가짜) 사용자를 만들어서 사용
            - roles에 권한 추가
            - 즉, 이 어노테이션으로 인해 ROLE_USER권한을 가진 사용자가 API를 요청하는 것과 동일한 효과
    - @SpringBootTest에서 MockMvc를 사용하는 방법
        - @Before("")
            - 매번 테스트가 시작되기 전에 MockMvc 인스턴스 생성
        - mvc.perform
            - 생성된 MockMvc를 통해 API 테스트
            - 본문 영역은 문자열로 표현하기 위해 ObjectMapper를 통해 문자열 JSON으로 변환
        
        ```Java
        @Autowired
            private WebApplicationContext context;
        
            private MockMvc mvc;
        
            @Before("")
            public void setup(){
                mvc = MockMvcBuilders
                        .webAppContextSetup(context)
                        .apply(springSecurity())
                        .build();
            }
        
            @Test
            @WithMockUser(roles="USER")
            public void Posts_등록된다() throws Exception{
                ...
        
                //when
                mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                        .andExpect(status().isOk());
        
                //then
                List<Posts> all = postsRepository.findAll();
                assertThat(all.get(0).getTitle()).isEqualTo(title);
                assertThat(all.get(0).getContent()).isEqualTo(content);
        
            }
        
            @Test
            @WithMockUser(roles="USER")
            public void Posts_수정된다() throws Exception{
                ...
        
                //when
                mvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(new ObjectMapper().writeValueAsString(requestDto)))
                        .andExpect(status().isOk());
                //then
                List<Posts> all = postsRepository.findAll();
                assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
                assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
            }
        ```
        
3. @WebMvcTest에서 CustomOAuth2UserService를 찾을 수 없음
    1. @WebMvcTest는 CustomOAuth2UserService를 스캔하지 않기 때문
    2. @WebMvcTest는 WebSecurityconfigurerAdapter, WebMvcConfigurer를 비롯한 @Controller, @Controller를 읽음
    3. 즉, @Repository,@Service, @Component는 스캔 대상이 아님
    4. SecurityConfig는 읽었지만 SecurityConfig를 생성하기 위해 필요한 CustomOAuth2UserService는 읽을 수가 없어 앞에서와 같은 에러 발생
    5. 스캔대상에서 SecurityConfig제거
        
        ```Java
        @WebMvcTest(controllers = HelloController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
        ```
        
    6. `@WithMockUser(roles = "USER")` 추가
4. @EnableJpaAuditing로 인한 에러 발생
    1. @EnableJpaAuditing를 사용하기 위해선 최소 하나의 @Entity 클래스 필요
    2. Application.java에서 @EnableJpaAuditing 제거
    3. config 패키지에 JpaConfig 생성하여 @EnableJpaAuditing 추가
