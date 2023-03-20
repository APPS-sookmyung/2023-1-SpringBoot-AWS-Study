스프링 부트와 AWS로 혼자 구현하는 웹 서비스

# Week 1

## Chap01. 인텔리제이로 스프링 부트 시작하기

### Gradle 설정하기

**build.gradle 내용이 책과 많이 달라져서 따로 조사하여 정리하겠습니다.**

> 참고로 저는 스프링 부트 3.0.4 가장 최신 버전으로 사용하고 있답니다!

전체 기본 build.gradle

```gradle
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.0.4'
	id 'io.spring.dependency-management' version '1.1.0'
}

group = 'com.apps'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
	useJUnitPlatform()
}
```

1. plugins
   - 특정 작업을 하기 위해서 모아놓은 Task들의 묶음.
     - [org.springframework.boot](https://plugins.gradle.org/plugin/org.springframework.boot) : Core Spring Boot Classes. 스프링 기반 Application 제작을 위한 기본적인 클래스들.
     - [io.spring.dependency-management](https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/) : A Gradle plugin that provides Maven-like dependency management functionality. Maven과 같이 의존성 관리 기능을 제공하는 Gradle 플러그인.
2. [repositories](https://docs.gradle.org/current/userguide/declaring_repositories.html)
   - 각종 프로그램들이 저장되는 위치. 여기서는 `mavenCentral()`을 통해 Maven 중앙 저장소를 사용해줌.
3. dependencies
   - 의존 라이브러리. 저장소에서 필요한 라이브러리를 사용하기 위해서.
   - [spring-boot-starter-web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web) : Starter for building Web, including RESTful, applications for using Spring MVC
     - spring-boot-starter-tomcat: 톰캣 (웹서버)
     - spring-webmvc
   - [spring-boot-starter-test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test) : Starter for testing Spring Boot applications with libraries including JUnit Jupiter, Hamcrest and Mockito -> 최근엔 JUnit5를 주로 사용.
     - junit : 테스트 프레임워크.
     - mockito : 목 라이브러리
     - [assertj](https://joel-costigliola.github.io/assertj/index.html) : 테스트 작성 용이하게 도와주는 라이브러리
     - spring-test : 스프링 통합 테스트 지원
4. tasks.named('test')
   - JUnit5(테스트 프레임워크) 사용을 위한 설정.

---

## Chap02. 스프링 부트에서 테스트 코드를 작성하자

### 테스트 코드 소개

- TDD : 테스트 주도 개발

  - 즉, 테스트 코드를 먼저 작성하는 것 부터가 시작이다!

- 레드 그린 사이클
  - Red: 항상 실패하는 테스트를 먼저 작성
  - Green: 테스트가 통과하는 프로덕션 코드 작성
  - Refactor: 테스트 통과 후 프로덕션 코드 리팩토링
- 단위 테스트: 기능 단위의 테스트 코드 작성.
  - 빠른 피드백 가능
  - 개발자가 만든 기능 보호

### Controller API 작성하기

> 애노테이션 위주로 정리하였습니다. 코드는 파일 참고!

- @RestController : 컨트롤러를 JSON을 반환하는 컨트롤러로 만들어 줌.

  - @ResponseBody + @Controller

- @GetMapping : Get 메서드

### Controller 테스트 코드 작성하기

> 책에서는 JUnit4로, 저는 최신 버전인 JUnit5로 테스트 코드를 작성하여 문법의 차이부터 새로운 애노테이션들을 정리하였습니다.

```java
@ExtendWith(SpringExtension.class)  // JUnit5에서는 RunWith가 ExtendWith로 변경됨. - 생략 가능
@WebMvcTest(controllers = HelloController.class)
class HelloControllerTest { ... }
```

- @ExtendWith(SpringExtension.class) : JUnit5에서는 RunWith가 ExtendWith로 변경되었습니다.
  - 참고로 스프링 부트에서 제공하는 모든 테스트용 애노테이션에 meta 애노테이션으로 적용되어 생략이 가능하다고 합니다~!
- @WebMvcTest(controllers = HelloController.class) : 여러 스프링 어노테이션 중 Web(Spring MVC)에 집중할 수 있는 애노테이션입니다. (여기서는 컨트롤러만 사용하기 때문에 선언하고 사용해줌.)
  - JUnit5에서는 @SpringBootTest 애노테이션을 많이 사용하는데 <br>
    => 이는 통합 테스트에서 주로 사용합니다. (무겁고 시간이 오래 걸림.) 참고로 둘이 같이 쓰면 오류가 발생합니다.
