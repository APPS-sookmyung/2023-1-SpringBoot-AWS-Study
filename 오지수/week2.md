스프링 부트와 AWS로 혼자 구현하는 웹 서비스

# Week 2

## chap03. 스프링부트에서 JPA로 데이터베이스를 다뤄보자

### 3.1 JPA 소개

최근엔 객체를 관계형 데이터 베이스에서 관리하는 것이 무엇보다 중요함.

- JPA 등장 이전

  - 자바 클래스를 아무리 아름답계 설계해도, DB 작업을 위해 SQL을 계속 사용해야 했음.
  - **패러다임 불일치** 문제로 애초에 객체를 DB에 저장한다는 것 자체가 어려움.

<br>

#### **JPA**

- 객체지향과 RDB 사이의 패러다임을 일치시키기 위한 기술.
- 개발자는 객체지향적으로 프로그래밍을 하면, JPA가 이를 RDB에 맞게 SQL을 대신 생성해서 실행시킴.
- **SQL 종속적 개발**에서 벗어나게 됨.

<br>

#### **Spring Data JPA**

원래 JPA를 사용하기 위해선 Hibernate와 같은 구현체가 필요함. <br>
스프링에서는 이 구현체를 좀 더 쉽게 사용하고자 추상화 시킨 **Spring Data JPA** 사용을 권장중.

<br>

- Spring Data JPA 등장 이유
  1. 구현체 교체의 용이성 : Hibernate 외의 다른 구현체로 쉽게 교체하기 위함.
  2. 저장세 교체의 용이성 : RDB 외에 다른 저장소로 쉽게 교체하기 위함.

<br>

#### **실무에서의 JPA**

가장 큰 문제점 = **높은 러닝 커브**
JPA를 쓰기 위해선? **RDB와 DB 모두** 이해해야 함

하지만 너무 유용한 장점들...

- 쿼리 작성 수 하락
- 속도 이슈 적음.

<br>

#### **요구사항 분석**

- 게시판 기능
  - 게시글 조회
  - 게시글 등록
  - 게시글 수정
  - 게시글 삭제
- 회원 기능
  - 구글 / 네이버 로그인
  - 로그인한 사용자 글 작성 권한
  - 본인 작성 글에 대한 권한 관리

---

### 3.2 프로젝트에 Spring Data JPA 적용하기

#### 애노테이션 위주 정리

- @Entity: 테이블과 링크될 클래스임을 나타냄.
- @Id : 해당 테이블의 PK 필드를 나타냄.
- @GeneratedValue : PK의 생성 규칙을 나타냄
  - strategy = GenerationType.IDENTITY <br>
    auto_increment
- @LocalServerPort : 테스트에 포트 번호 주입

### JPA Auditing

- BaseTimeEntity 클래스를 사용하기.

#### 애노테이션

- @MappedSuperclass: JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 인식함.
- @EntityListeners(AuditingEntityListener.class) : BaseTimeEntity 클래스에 Auditing 기능을 포함시킴.
- @CreatedDate: Entity가 생성되어 저장될 때 시간이 자동 저장됨.
- @LastModifiedDate: 조회한 Entity의 값을 변경할 때 시간이 자동 저장됨.
- @EnableJpaAuditing: JPA Auditing 활성화. (Main Application 위에 작성하기!)
