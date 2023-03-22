# WEEK1
## Chap01. 인텔리제이로 스프링 부트 시작하기
> 추후 쿼리 작성을 위해 학교 인증 받아서 IntelliJ Ultimate 설치 (책 버전과 동일하게 하기 위해 2022.2.5 버전 다운)

### Gradle 프로젝트를 스프링 부트 프로젝트로 변경하기
```java
buildscript {
    ext { // 전역변수 설정
        springBootVersion = '2.1.7.RELEASE'
    }
    repositories { // 의존성들을 어떤 원격 저장소에서 받을지 결정
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

// 의존성 적용
apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management' // 스프링 부트의 의존성을 관리해주는 플러그인

//plugins {
//    id 'java'
//}

group 'org.example'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web')
    testImplementation('org.springframework.boot:spring-boot-starter-test')

//    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
//    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
}

//test {
//    useJUnitPlatform()
//}
```
* ext : build.gradle 에서 사용하는 전역변수를 설정
* io.spring.dependency-management : 스프링 부트의 의존성들을 관리해주는 플러그인 -> 반드시 추가해야 함
> compile, runtime, testCompile, testRuntime 은 Gradle 7.0 (2021.4.9) 부터 삭제되었으므로 각각 implementation, runtimeOnly, testImplementation, testRuntimeOnly 으로 대체하여 오류 해결
* repositories : 각종 의존성(=라이브러리)들을 어떤 원격 저장소에서 받을지 결정 (기본적으로 mavenCentral이나, 라이브러리 업로드 난이도로 jcenter 사용)

### 인텔리제이에서 깃과 깃허브 사용하기
프로젝트 실행시 자동으로 생성되는 파일들이 있는 .idea 디렉토리는 커밋 x

## Chap02. 스프링 부트에서 테스트 코드를 작성하자
* 테스트 코드 작성 이유: 자동검증 + 개발자가 만든 기능 보호 가능 + 문제 조기 발견

#### HelloController 생성 및 롬복으로 전환
* 패키지
  * main
    * web: 컨트롤러와 관련된 클래스들을 담을 패키지 (HelloController) (1)
      * dto: 모든 응답 Dto 추가할 패기지 (HelloResponseDto) (3)
  * test: 테스트 클래스들을 담을 패키지 (HelloControllerTest) (2)
    * dtoTest (HelloResponseDtoTest) (4)
* 어노테이션
  * @SpringBootApplication: 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정 (프로젝트의 최상단에 위치해야 함) -> Application (프로젝트의 메인 클래스)
  * @RestController: JSON 반환하는 컨트롤러 생성 -> 1
  * **@GetMapping:** HTTP Method인 Get의 요청을 받을 수 있는 API 생성 -> 1
  * @RunWith(SpringRunner.class): 테스트 진행시, JUnit에 내장된 실행자 외에 다른 실행자 실행(=스프링 부트 테스트와 JUnit 연결자) -> 2
  * @WebMvcTest: Web에 집중할 수 있는 어노테이션 (@Servie, @Component 등 사용 가능) -> 2
    @WebMvcTest(controllers = HelloController.class) // 컨트롤러 지정
  * **@Autowired**: 스프링이 관리하는 빈(Bean) 주입 -> 2
  * @Getter: get메소드 생성 -> 3
  * @RequiredArgsConstructor: 모든 final 필드가 포함된 생성자를 생성 -> 3
  * @RequestParam: 외부에서 API로 넘긴 파라미터를 가져옴 -> 1

* private MockMvc mvc: 스프링 MVC 테스트의 시작점

HelloController: /hello로 요청이 오면 문자열 hello 반환
HelloControllerTest: /hello 주소로 HTTP GET 요청 (mvc.perform(get("/hello")
.andExpect(status().isOk()): mvc.perform의 결과를 검증
.andExpect(content().string(hello)): Controller에서 "hello"를 리턴하는데 맞는지 검증
