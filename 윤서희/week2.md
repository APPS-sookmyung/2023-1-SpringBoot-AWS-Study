# Week2
## Chap 03. 스프링 부트에서 JPA로 데이터베이스 다뤄보자
* JPA: 자바 표준 ORM (Object Relational Mapping)
* ORM은 객체를 매핑, SQL Mapper는 쿼리를 매핑
### 1. JPA 소개
 * JPA: 객체지향 프로그래밍 언어와 관계형 데이터베이스 중간에서 패러다임 일치를 시켜주기 위한 기술
=> 객체 지향 프로그래밍 후 JPA가 SQL을 대신 생성해서 실행 -> SQL에 종속적인 개발 X
* Spring data JPA
    * 구현체 교체의 용이성: Hibernate 외의 다른 구현체로 쉽게 교체하기 위함
    * 저장소 교체의 용이성: 관계형 데이터베이스 외에 다른 저장소로 쉽게 교체하기 위함 (ex. 의존성만 교체)
=> 이 두 용이성이 가능한 이유: Spring data의 하위 프로젝트들은 기본적인 CRUD의 인터페이스가 같음 (Create, Read, Update, Delete)
* 요구사항 분석
    * 게시판 기능
       1) 게시글 조회
       2) 게시글 등록
       3) 게시글 수정
       4) 게시글 삭제
     * 회원 기능
       1) 구글/네이버 로그인
       2) 로그인한 사용자 글작성 권한
       3) 본인 작성 글에 대한 권리
### 2. 프로젝트에 Spring Data Jpa 적용하기
#### build.gradle
``` java
implementation('org.springframework.boot:spring-boot-starter-data-jpa') // JPA 관련 라이브러리 버전 관리
implementation('com.h2database:h2') // 인메모리 관계형 데이터베이스
메모리에서 실행 > 애플리케이션 재시작할 때마다 초기화 (테스트 용도로 많이 사용)
```
#### domain 패키지
도메인을 담을 패키지
* 도메인: 게시글, 댓글, 회원 등 소프트웨어에 대한 요구사항 혹은 문제 영역
#### domain.posts 패키지
#### Posts 클래스 (Entity 클래스) 
DB의 테이블과 매칭될 클래스
* 어노테이션 정리
   * @Entity: JPA 어노테이션 (주요 어노테이션, 데이터베이스와 맞닿은 핵심 클래스)
   * 롬복 어노테이션 (필수 X)
       * @NoArgsConstructor: 기본 생성자 자동 추가 (=public Posts(){})
       * @Getter: 클래스 내 모든 필드의 Getter 메소드 자동생성
       * @Builder: 해당 클래스의 빌더 패턴 클래스 생성, 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
" Entity 클래스에서는 Setter 메소드를 만들지 않는다. "
> 목적과 의도를 나타낼 수 있는 메소드만 추가
> 생성자 대신 @Builder를 통해 제공되는 빌더 클래스를 사용 -> 생성자와 달리 어느 필드에 어떤 값을 채워야할지 명확하게 인지할 수 있음
``` java
Example.builder()
      .a(a)
      .b(b)
      .build();
```
#### PostsRepository 인터페이스
``` java
PostsRepository<Posts, Long> (=JpaRepository<Entity 클래스, PK타입>, DB Layer 접근자)
```

### 3. Spring Data JPA 테스트 코드 작성하기
#### PostsRepositoryTest
-> save, findAll 기능 테스트
* 어노테이션
   * @After: JUnit에서 단위 테스트가 끝날 때마다 수행되는 메소드 지정 (데이터가 남아있어 테스트 실행시 실패할 수 있음)

### 4. 등록/수정/조회 API 만들기
필요한 클래스: 1) Request 데이터를 받을 Dto, 2) API 요청을 받을 Controller, 3) 트랜잭션, 도메인 기능 간의 순서를 보장하는 Service
#### Spring 웹 계층
Web Layer: 뷰템플릿 영역 (@controller..), 외부 요청과 응답
Service Layer: @Service에 사용되는 서비스 영역, Controller와 Dao의 중간영역, @Transactional이 사용되어야 하는 영역
Repository Layer: 데이터 저장소에 접근하는 영역, Dao(Data Access Object 영역)

Dtos(Data Transfer Object): 계층 간에 데이터 교환을 위한 객체의 영역 ex. Reposptry Layer에서 결과로 넘겨준 객체 등
Domain Model: 모든 사람이 동일한 관점에서 이해할 수 있고 공유할 수 있도록 단순화 시킨 것 -> 비즈니스 처리를 담당
#### PostsApiController
#### PostsService
* 스프링 빈 주입 방법: 1) Autowired, 2) setter, 3) 생성자
* @Autowired 대신 생성자로 Bean 객체를 받음 (-> @RequiredConstructor가 해결)
  * 클래스의 의존성 관계가 변경될 때마다 생성자 코드를 계속해서 수정하는 번거로움 해결
#### PostsApiControllerTest
* @WebMvcTest 사용 안 한 이유: JPA 기능 작동 X
* Controller, ControllerAdvice: 외부 연동과 관련된 부분만 작성 -> (JPA 기능까지 한번에 테스트하기 위해서) @SpringBootTest + TestRestTemplate 사용
* Application 클래스의 메소드 실행 후 -> 톰캣을 8080 포트로 실행
#### PostsRequestDto
* Entity를 받아 처리

### 5. JPA Auditing으로 생성시간/수정시간 자동화하기
#### domain 패키지 -> BaseTimeEntity 클래스 생성
* 어노테이션 정리
   * @MappedSupperclass: BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 인식
   * @EntityListeners(AuditingEntityListener.class): BaseTimeEntity에 Auditing 기능 (=시간에 대해 자동으로 값 부여하는 기능) 포함
   * @CreatedDate: Entity가 생성되어 저장될 때 시간이 자동저장됨
   * @LastModifiedDate: 조회한 Entity의 값을 변경할 때 시간이 자동저장됨
#### Posts 클래스가 BaseTimeEntity 상속받도록 변경
#### Application 클래스
@EnableJpaAuditing: JPA Auditing 어노테이션들을 모두 활성화할 수 있도록 함
#### JPA Auditing 테스트 코드 -> PostsRepositoryTest 클래스에 추가
* BaseTimeEntity_등록 메소드 추가

---
### 2주차 과제 내용
* Delete 삭제 내용 추가
  - PostsApiController: @DeleteMapping 작성
  - PostsService: ApiController에 사용된 메소드 정의
  > PostsService -> PostsApiController 사용
  - PostsApiControllerTest: @Test 코드 추가 (삭제 기능이므로 반환값 X)
