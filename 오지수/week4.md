스프링 부트와 AWS로 혼자 구현하는 웹 서비스

# Week 4

## chap05. 스프링 시큐리티와 OAuth 2.0으로 로그인 기능 구현하기

### 구글 서비스 등록하기

- 구글 클라우드에서 API 사용설정을 해준 뒤 `application-oauth.properties`에서 클라이언트 ID와 클라이언트 보안 비밀 코드를 다음과 같이 등록해줌.

```
spring.security.oauth2.client.registration.google.client-id=
spring.security.oauth2.client.registration.google.client-secret=
spring.security.oauth2.client.registration.google.scope=profile, email
```

- application.yml에 다음 코드 추가

```
spring:
  profiles:
    include: oauth
```

---

### 구글 로그인 연동하기

- 코드는 생략하고 바뀐 부분에 따른 오류만 정리함.

1. Table 생성 오류

   ```java
   @Table(name = "users")
   ```

   위와 같이 name을 설정해주세요!!

   - H2 데이터베이스 특정 버전에선 user는 reserved keyword입니다!
     @Table annotation을 사용해서 DB Table 이름을 변경해주어야 해요!

2. spring jdbc session 설정
   - applicatin.yml에서 다음과 같이 설정하기 : SPRING_SESSION 테이블 생성 에러를 방지합니다.
   ```
   spring:
     session:
    store-type: jdbc
    jdbc:
      initialize-schema: always # SPRING_SESSION Table 생성 에러
   ```
3. SecurityConfig의 코드

- WebSecurityConfigurerAdapter가 deprecated되면서 SecurityFilterChain을 사용한 config를 Bean으로 등록하여 사용해야 됩니다. <br>
  바뀐 코드는 다음과 같습니다.

```java

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    // spring security 적용 하지 않을 URL 리스트
    private static final String[] AUTH_WHITELIST = {
            "/",
            "/css/**",
            "/image/**",
            "/js/**",
            "/h2-console/**"
    };

    // 인증 필요 리스트
    private static final String[] VERIFICATION_AUTH_LIST = {
            "/api/v1/**"
    };

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션들을 disable 처리
                .and()
                .authorizeHttpRequests( // antMatchers(), mvcMatchers(), regexMatchers()가 -> authorizeHttpRequests() 또는 securityMatchers()로 변경
                        request -> request
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers("/", "/css/**", "/image/**", "/js/**", "/h2-console/**").permitAll()
                                .requestMatchers(VERIFICATION_AUTH_LIST).hasRole(Role.USER.name())
                                .anyRequest().authenticated()
                )
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
        return http.build();
    }
}

```

4. User 클래스가 아닌 DTO를 사용해야 하는 이유
   - User클래스에 직렬화를 구현하지 않았다는 에러가 발생함. <br>
     User클래스는 엔티티이기 때문에 `@OneToMany`, `@ManyToMany` 등 자식 엔티티를 여러개 가지고 있을 경우 성능 이슈, 부수 효과가 발생할 가능성이 높음. <br>
     그러므로 직렬화 기능을 가진 DTO를 새로 만드는게 이후 유지보수에 유리하다!

---

### 어노테이션 기반으로 개선하기

같은코드가 반복되는 부분을 `@LoginUser` 어노테이션을 사용하여 방지한다.

```java
// 같은 코드 부분
SessionUser user = (SessionUser) httpSession.getAttribute("user");
```

- 다음과 같이 구현하여 해결해줌.

```java
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}

```

---

### 네이버 로그인 구현하기

- 네이버 API 설정 후, 스프링 시큐리티 설정만 OAuthAttributes에 등록해주면 됨.
