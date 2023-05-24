# WeeK4

## Chap05. 스프링 시큐리티와 oaUTH 2.0으로 로그인 기능 구현하기

### 5.1 스프링 시큐리티와 스프링 시큐리티 Oauth2 클라이언트

- 소셜 로그인 기능을 보통 사용 ⇒ 서비스 개발에 집중
- Spring Boot 1.5 vs Spring Boot 2.0
    - 1.5 방식에서는 url 주소 모두 명시/2.0 방식에서는 client 인증 정보만 입력하면 된다.
    - CommonOAuth2Provider라는 **enum**이 새롭게 추가되어 구글, 깃허브, 페이스북, 옥타의 기본 설정값 제공 (이외 다른 소셜 로그인을 추가하면 직접 다 추가)

### 5.2 구글 서비스 등록

- `clientId, clientSecret` 를 통해 로그인 기능과 소셜 서비스 기능을 사용할 수 있으므로 무조건 발급받고 시작 → 구글 클라우드 플랫폼 주소로 이동
- 승인된 리디렉션 URI
    - 서비스에서 파라미터로 인증 정보를 주었을 때 인증이 성공하면 구글에서 리다이렉트할 URL
    - {도메인}/login/oauth2/code/{소셜서비스코드} URL 지원
    - 시큐리티에서 이미 구현해놓은 상태이므로 URL을 지원하는 컨트롤러를 만들 필요 없다.
- .gitignore 등록
    - [application-oauth.properties](http://application-oauth.properties) 파일이 깃허브에 올라가는 것을 방지
    - `[application-oauth.properties](http://application-oauth.properties)` 입력

### 5.3 구글 로그인 연동하기

> **user 패키지**
> 
- User 클래스 : 사용자 정보 담당
    - Enumerated(EnumType.STRING)
        - JPA로 데이터베이스로 저장할 때 Enum 값을 어떤 형태로 저장할지 결정
        - default: int로 된 숫자 ⇒ 문자열로 저장될 수 있도록 선언
- Role 클래스 : 사용자의 권한을 관리할 Enum 클래스
    - 권한 코드에 항상 ROLE_이 앞에 있어야만 한다. ⇒ ROLE_GUEST, ROLE_USER 지정
- UserRepository : User의 CRUD 책임
    - findByEmail
        - email을 통해 이미 생성된 사용자인지 처음 가입하는 사용자인지 판단하는 메소드 설정
        - `Optional<User> findByEmail(String email);`

> **************스프링 시큐리티 설정**************
> 
- 시큐리티 관련 의존성 추가

> **config.auth 패키지 : 시큐리티 관련 클래스를 담는 패키지**
> 
- **SecurityConfig 클래스**
    - @EnableWebSecurity
        
        Spring Security 설정들을 활성화
        
    - authorizeRequests
        
        URL별 권한 관리를 설정하는 옵션의 시작점 ⇒ antMatchers 옵션 사용
        
    - antMatchers
        - 권한 관리 대상을 지정하는 옵션
        - URL, HTTP 메소드별로 관리 가능
        - permitAll() 옵션을 통해 전체 열람 권한 부여
    - anyRequest
        - 설정된 값들 이외 나머지 URL들을 나타낸다.
        - authenticated()을 추가하여 나머지 URL들을 모두 인증된 사용자 (= 로그인한 사용자)에게만 허용하게 한다.
    - `logout().logoutSuccessUrl("/")`
        - 로그아웃 기능에 대한 여러 설정의 진입점
        - 로그아웃 성공 시 / 주소로 이동
    - oauth2Login
        
        OAuth2 로그인 기능에 대한 여러 설정의 진입점
        
    - userInfoEndpoint
        
        OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
        
    - userService
        
        소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
        
- **CustomOAuth2UserService 클래스**
    - registrationId
        
        현재 로그인 진행 중인 서비스를 구분하는  코드
        
    - userNameAttributeName
        - OAuth2 로그인 진행 시 키가 되는 필드값
        - 네이버, 구글 로그인을 동시 지원할 때 사용
    - **OAuthAttributes 클래스**
        
        OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        
    - **SessionUser 클래스**
        
        세션에 사용자 정보를 저장하기 위한 Dto 클래스
        
- **************************************************OAuthAttributes 클래스**************************************************
    - of
        
        OAuth2User에서 사용자 정보를 Map으로 반환하기 때문에 값 하나 하나를 변환
        
    - toEntity
        - User 엔티티 생성
        - 처음 가입할 때 OAuthAttributes에서 엔티티를 생성
- **SessionUser 클래스**
    
    인증된 사용자 정보만 필요 (User 클래스와 별개여야 함)
    

> **********************로그인 테스트**********************
> 
- index.mustache
    
    머스테치는 true/false 여부만 판단 → 최종값을 넘겨줘야 한다.
    
    - a href=”/logout”
        
        스프링 시큐리티에서 기본적으로 제공하는 로그아웃 URL
        
    - {{^userName}}
        - 머스테치에서 해당 값이 존재하지 않는 경우에 ^ 사용
        - userName이 없다면 로그인 버튼을 노출시키도록 구성
    - a href=”/oauth2/authorization/google”
        
        스프링 시큐리티에서 기본적으로 제공하는 로그인 URL
        

### 5.4 어노테이션 기반으로 개선하기

> **@LoginUser 어노테이션**
> 
- @Target(ElementType.PARAMETER)
    - 이 어노테이션이 생성될 수 있는 위치를 지정
    - 메소드의 파라미터로 선언된 객체에서만 사용 가능
- @interface
    - 이 파일을 어노테이션 클래스로 지정

> **config 패키지**
> 
- WebConfig 클래스

### 5.5 세션 저장소로 데이터베이스 사용하기

- 세션 저장소 종류
    1. 톰캣 세션 사용
    2. MySQL과 같은 **데이터 베이스**를 세션 저장소로 사용
        
        ⇒ 설정이 간단하고 사용자가 많은 서비스가 아니며 비용 절감을 위해서 선택
        
    3. Redis, Memached와 같은 메모리 DB를 세션 저장소로 사용

### 5.6 네이버 로그인

- 네이버에서는 스프링 시큐리티를 공식 지원하지 않음
    
    ⇒ Common-OAuth2Provider에서 해주던 값들을 전부 수동으로 입력 (application-oauth.properties에 등록)
    
    ```java
    spring.security.oauth2.client.provider.naver.user-name-attribute=response
    ```
    
    → 네이버의 회원 조회시 반환되는 JSON 형태 때문에 기준이 되는 user_name의 이름을 네이버에서는 response로 해야 한다.
    
    → 스프링 시큐리티에선 하위 필드를 명시 X, 네이버의 응답값 최상위 필드는 resultCode, message, response ⇒ 이 프로젝트에서는 response를 user_name으로 지정하고 이후 **response의 id를 user_name으로 지정**
    
- 스프링 시큐리티 설정 등록
    - OAuthAttributes에 네이버인지 판단하는 코드와 네이버 생성자만 추가
    - index.mustache에 네이버 로그인 버튼 추가
        
        /oauth2/authorization/naver
        
        ⇒ URL은 application-oauth.properties에 등록한 redirect-uri 값에 맞춰 자동으로 등록
        
        ⇒ /oauth2/authorization/ 까지 고정이고 마지막 Path만 각 소셜 로그인 코드 사용 (여기서는 naver 를 사용하기 때문에 마지막 Path가 naver)
        

### 5.7 기존 테스트에 시큐리티 적용하기

- 시큐리티 옵션이 활성화되면 인증된 사용자만 API 호출할 수 있다.
    
    ⇒ 테스트 코드마다 인증한 사용자가 호출한 것처럼 작동하도록 수정
    

> 문제1 : CustomOAuth2UserService를 찾을 수 없음
> 
- CustomOauth2UserService를 생성하는데 필요한 소셜 로그인 관련 설정값들이 없기 때문에 발생
- application-oauth.properties에 설정값들을 추가했는데 설정이 없는 이유
    
    ⇒ src/main/resources/application.properties가 테스트 코드를 수행할 때 적용되는 이유는 test에 application.properties가 없으면 main의 설정을 그대로 가져오기 떄문
    
    <aside>
    ❓ 자동으로 가져오는 옵션의 범위는 [application.properties](http://application.properties) 파일까지
    
    </aside>
    
    ⇒ 즉 application-oauth.properties는 test에 파일이 없다고 가져오는 파일이 아니다.
    

> 문제2: 302 Status Code
> 
- **스프링 시큐리티 설정** 때문에 **인증되지 않은 사용자**의 요청을 이동시키기 때문에 임의로 인증된 사용자를 추가하여 API를 테스트해야 한다.
    
    ⇒ spring-security-test를 build.gradle에 추가
    
    ⇒ PostsApiControllerTest에 사용자 인증 추가
    
    `@WithMockUser(roles=”USER”)`
    
    - 인증된 모의(가짜) 사용자를 만들어서 사용
    - ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가지게 된다.
    - MockMvc 에서만 작동하기 때문에 @SpringBootTest에서 MockMvc를 사용하는 방법을 적용해야 함

> 문제3: @WebMvcTest 에서 CustomOAuth2UserService을 찾을 수 없음
> 
- @WebMvcTest 는 @Repository, @Service, @Component 를 스캔하지 X ⇒ SecurityConfig를 읽었지만 SecurityConfig를 생성하기 위해 필요한 **CoustomOAuth2UserService는 읽을 수 없음**
    
    ⇒ 스캔 대상에서 SecurityConfig 제거
