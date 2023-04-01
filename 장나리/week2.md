# WEEK 2

## CHAPTER 03 스프링 부트에서 JPA로 데이터베이스 다뤄보자

웹 서비스를 개발하고 운영하다 보면 피할 수 없는 문제가 **데이터베이스를 다루는 일**

어떻게 관계형 데이터베이스를 이용하는 프로젝트에서 객체지향 프로그래밍을 할 수 있을까? → **JPA라는 자바 표준 ORM**(Object Relational Mapping) 기술

### 3.1 JPA 소개

현대의 웹 어플리케이션에서 관계형 데이터베이스는 빠질 수 없는 요소 → 객체를 관계형 데이터베이스에서 관리하는 것이 매우 중요

oracle, MySQL, MSSQL 등등

프로젝트에서 SQL이 중심이 됨

문제점

- SQL을 통해야만 데이터베이스에 저장하고 조회할 수 있어 반복적으로 SQL을 만들어야 함
- 패러다임 불일치
    - 관계형 데이터베이스는 어떻게 데이터를 저장할지에 초점을 맞추고 있음
    - 객체지향 프로그래밍언어는 기능과 속성을 한 곳에서 관리하는 기술
    - 언어의 패러다임이 다른데 객체를 데이터 베이스에 저장하려고 하니 여러 문제 발생

JPA는 이러한 문제점을 해결하기 위해 등장

- 서로 지향하는 바가 다른 2개 영역(객체지향 + 관계형 데이터 베이스)을 중간에서 패러다임 일치
- 더는 SQL에 종속적인 개발을 하지 않아도 됨

**Spring Data JPA**

JPA 

- 인터페이스로서 자바 표준 명세서
- 사용하기 위해 구현체 필요
- 대표적으로 Hibernate, Eclipse, Link 등
- 하지만 spring에서 JPA를 사용할 때는 구현체를 직접 다루지는 않음
- 구현체들을 좀 더 쉽게 사용하고자 추상화시킨 Spring data JPA라는 모듈 사용
    - JPA ← Hibernate ← Spring data JPA
- Hibernate와 Spring Data JPA의 큰 차이는 없지만 Spring Data JPA가 등장한 이유
    - 구현체 교체의 용이성
        - Hibernate 외에 다른 구현체로 쉽게 교체
    - 저장소 교체의 용이성
        - 관계형 데이터베이스 외에 다른 저장소를 쉽게 교체하기 위함

**실무에서 JPA**

실무에서 JPA를 사용하지 못하는 이유 : 높은 러닝 커브

JPA를 잘 쓰려면 객체지향 프로그래밍과 관계형 데이터베이스를 둘 다 이해해야 함

장점

- CRUD 쿼리 작성할 필요 없음
- 객체지향 프로그래밍을 쉽게 할 수 있음

**요구사항 분석**

- 게시판 기능
    - 게시글 조회
    - 게시글 등록
    - 게시글 수정
    - 게시글 삭제
- 회원 기능
    - 구글 / 네이버 로그인
    - 로그인한 사용자 글 작성 권한
    - 본인 작성 들에 대한 권한 관리

---

### 3.2 프로젝트에 Spring Data JPA 적용하기

그레이들에 추가

```java
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'com.h2database:h2'
```

- spring-boot-starter-data-jpa
    - 스프링 부트용 Spring Data Jpa 추상화 라이브러리
- h2
    - 인메모리 관계형 데이터베이스
    - 별도의 설치 없이 프로젝트 의존성만으로 관리 가능
    - 메모리에서 실행되기 때문에 애플리케이션을 재실행할 때마다 초기화 된다는 점을 이용해 테스트 용도로 많이 사용

domain 패키지 : 도메인을 담을 패키지

- 도메인 : 게시글, 댓글, 회원, 정산, 결제 등 소프트웨어에 대한 요구사항 혹은 문제 영역

Posts 클래스

- @Entity
    - 테이블과 링크될 클래스임을 나타냄
    - 기본 값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭
    - ex) [SalesManager.java](http://SalesManager.java) → sales_manager table
- @Id
    - 해당 테이블의 PK 필드를 나타냄
- @GeneratedValue
    - PK의 생성 규칙
- @Column
    - 테이블의 칼럼, 굳이 선언하지 않아도 해당 클래스의 필드는 모두 칼럼이 됨
    - 사용하는 이유는 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용
- @NoArgsConstructor
    - 기본 생성자 자동 추가
    - `public Posts(){}`와 같은 효과
- @Getter
    - 클래스 내 모든 필드의 Getter 메소드 자동생성
- @Builder
    - 해당 클래스의 빌더 패턴 클래스 생성
    - 생성자 상단에 선언 시 생성자에 포함된 빌드만 빌더에 포함
- Posts 클래스는 Setter 메소드가 없다.
    - getter/setter를 무작정 생성하면 해당 클래스의 인스턴스 값드링 언제 어디서 변해야하는지 코드상으로 구분 불가능
    - Entity 클래스에서는 절대 Setter 메소드를 만들지 않음
    - 그럼 어떻게 값을 채워 DB에 삽입하지 ?
        - → 기본적인 구조는 생성자를 통해 최종값을 채운 후 DB에 삽입
        - 값 변경이 필요하나 경우 해당 이벤트에 맞는 public 메소드를 호출하여 변경하는 것을 전제로 함
        - 생성자 대신에 @Builder를 통해 제공되는 빌더 클래스를 사용
        - 빌더를 사용하면 어느 필드에 어떤 값을 채워야할지 명확하게 인지 가능

PostsRepository

- DB Layer 접근자
- 인터페이스로 생성
- @Repository 추가할 필요 없음
- Entity 클래스와 기본 Entity Repository는 함께 위치해야함
- 나중에 프로젝트 규모가 커져 도메인별로 프로젝트를 분리해야한다면 도메인 패키지에서 관리

---

### 3.3 Spring Data JPA 테스트 코드 작성하기

PostsRepositoryTest

- @After
    - Junit에서 단위 테스트가 끝날 때마다 수행되는 메소드
    - 보통은 배포 전 전체 테스트를 수행할 때 테스트간 메소드 침범을 막기 위해 사용
    - 여러 테스트가 동시에 수행되면 h2에 데이터가 그대로 남아 있어 다음 테스트 실행시 테스트가 실패할 수 있음
- postsRepository.save
    - 테이블 posts에 insert/update 쿼리 실행
    - id값이 있다면 update가, 없다면 insert 쿼리 실행
- postsRepository.findAll
    - 테이블 posts에 있는 모든 데이터 조회
- 실제 실행된 쿼리를 로그로 보려면
    - src/main/resources 아래에 [application.properties](http://application.properties) 파일에 아래 코드 추가
    
    ```java
    spring.jpa.show-sql=true 
    #MySQL 버전으로 변경
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
    spring.jpa.properties.hibernate.dialect.storage_engine=innodb
    spring.datasource.hikari.jdbc-url=jdbc:h2:mem:testdb;MODE=MYSQL
    ```
    

---

### 3.4 등록/수정/조회 API 만들기

API를 만들기 위해 총 3개의 클래스 필요

1. Request 데이터를 받을 Dto
2. API 요청을 받을 Controller
3. 트랜잭션, 도메인 기능 간의 순서를 보장하는 Service

Service에서 비지니스 로직을 처리해야한다 ? : 전혀 그렇지 않음

- Service는 트랜잭션, 도메인 간 순서보장의 역할만 함

Spring 웹 계층

- Web Layer
    - 흔히 사용하는 컨트롤러와  JSP/Freemarker 등의 뷰 템플릿 영역
    - 필터, 인터셉터, 컨트롤러 어드바이스 등 외부 요청과 응답에 대한 전반적인 영역
- Service Layer
    - @Service에 사용되는 서비스 영역
    - 일반적으로 Controller와 Dao의 중간영역에서 사용됨
    - @Transactional이 사용되어야하는 영역
- Repository Layer
    - Database와 같이 데이터 저장소에 접근하는 영역
- Dtos
    - Data Transfer Object는 계층간에 데이터 교환을 위한 객체
        - 뷰 템플릿 엔진에서 사용될 객체나 Repository Layer에서 결과로 넘겨준 객체 등
- Domain Model
    - 도메인이라 불리는 개발 대상을 모든 사람이 동일한 관점에서 이해할 수 있고 공유할 수 있도록 단순화 시킨 것을 도메인 모델이라 함
    - @Entity가 사용된 영역 역시 도메인 모델
    - 다만, 무조건 데이터베이스의 테이블과 관계가 있어야만 하는것은 아님
    - VO처럼 값 객체들도 이 영역에 해당

스프링에서 Bean를 주입하는 방식

1. @AutoWired(권장하지 않음)
2. setter
3. 생성자(가장 권장)

Entity클래스를 Request/Response 클래스로 사용해서는 안됨

- Entity클래스는 데이터베이스와 맞닿은 핵심 클래스

- Entity 클래스를 기준으로 테이블이 생성되고 스키마가 변경됨. 

@WebMvcTest 사용하지 않음

- JPA기능이 작동하지 않기 때문

- @SpringBootTest나 TestRestTemplate 사용

수정 기능

- 데이터 베이스에 쿼리를 날리는 부분이 없다.

- JPA의 영속성 컨텍스트(엔티티를 영구 저장하는 환경)

조회 기능

- h2로 확인

- application.properties에서 `spring.h2.console.enabled=true` 추가

---

### 3.5 JPA Auditing으로 생성시간/수정시간 자동화하기

보통 엔티티에는 해당 데이터의 생성시간과 수정시간을 포함

매번 DB에 삽입, 갱신하기 전에 날짜 데이터를 등록/수정하는 코드가 들어감.

반복으로 인해 코드가 지저분해짐 → JPA Auditing 사용

LocalDate 사용

- 날짜 타입 사용

- LocalDate와 LocalDateTime

    - @MappedSuperclass
        - JPA Entity 클래스들이 BaseTimeEntity을 상속할 경우 필드(createdDate, modifiedDate)들도 칼럼으로 인식
    - @EntityListeners(AuditingEntityListener.class)
        - BaseTimeEntity 클래스에 Auditing 기능 포함시킴
    - @CreatedDate
        - Entity가 생성되어 저장될 대 시간이 자동 저장됨
    - @LastModifiedDate
        - 조회한 Entity의 값을 변경할 대 시간이 자동 저장됨
