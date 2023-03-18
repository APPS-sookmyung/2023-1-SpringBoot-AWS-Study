스프링 부트와 AWS로 혼자 구현하는 웹 서비스

# Week 1

## Chap01. 인텔리제이로 스프링 부트 시작하기

### Gradle 설정하기

**build.gradle 내용이 책과 많이 달라져서 따로 조사하여 정리하겠습니다.**

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

### Controller 테스트 코드 작성하기

#### @SpringBootApplication

- 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성
