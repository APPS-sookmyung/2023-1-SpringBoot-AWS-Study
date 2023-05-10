# Chap03

## 3.1 JPA 소개

### Intro

- 객체를 관계형 데이터 베이스에서 관리하는 것이 중요
- 관계형 데이터 베이스는 SQL만 인식
- 반복적인 SQL을 많이 만들어야 하고 유지보수 해야 함
- 패러다임 불일치 문제 발생
    - 관계형 데이터베이스는 어떻게 데이터를 저장할지에 초점이 맞춰진 기술
    - 객체지향 프로그래밍 언어는 메세지를 기반으로 기능과 속성을 한 곳에서 관리하는 기술
    - 패러다임이 서로 다른데 객체를 데이터베이스에 저장하려고 하면서 생기는 문제
- 웹 애플리케이션 개발은 점점 데이터 베이스 모델링에만 집중
    - 이를 해결하기위해 JPA 등장
    - 객체지향적인 프로그래밍이 가능하고 SQL에 종속적인 개발을 하지 않아도 됨

### JPA

- 인터페이스로서 자바 표준명세서
- JPA <= Hibernate <= Spring Data JPA
- Spring Data JPA
    - 구현체 교체의 용이성 : Hibernate 외에 다른 구현체로 쉽게 교체하기 위함
    - 저장소 교체의 용이성 : 관계형 데이터베이스 외에 다른 저장소로 쉽게 교체하기 위함, 의존성만 교체, CRUD 인터페이스가 같기 때문

### 실무

- 실무에서 JPA를 사용하지 못하는 가장 큰 이유 : 높은 러닝 커브
- JPA에서 얻는 보상 : CRUD 쿼리를 직접 작성할 필요 X, 객체 지향 프로그래밍 가능

## 3.2 프로젝트에 Spring Data Jpa 적용하기

1. build.gradle에 의존성 등록

```
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-data-jpa')
    implementation('com.h2database:h2')
    testImplementation('org.springframework.boot:spring-boot-starter-test')
}
```

- spring-boot-starter-data-jpa
    - 스프링 부트용 Spring Data Jpa 추상화 라이브러리
    - 스프링 부트 버전에 맞춰 자동으로 JPA관련 라이브러리들의 버전 관리
- h2
    - 인메모리 관계형 데이터베이스
    - 별도의 설치 없이 프로젝트 의존성만으로 관리 가능
    - 메모리에서 실행되기 때문에 애플리케이션을 재시작할 때마다 초기화된다는 점을 이용하여 테스트 용도로 많이 사용

2. domain 패키지 생성 => posts 패키지 생성 => posts 클래스 생성

- 도메인 : 게시글, 댓글, 회원, 정산, 결제 등 소프트웨어에 대한 요구사항 혹은 문제 영역

```
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Posts{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
```

- 주요 어노테이션을 클래스에 가깝게 두기, 코틀린 등의 새 언어로 전환으로 롬복이 더이상 필요 없을 경우 쉽게 삭제 가능
- Posts 클래스 : 실제 DB의 테이블과 매칭될 클래스, Entity 클래스
- @Entity : 테이블과 링크될 클래스임을 나타냄, 기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍으로 테이블 이름을 매칭
- @Id : 해당 테이블의 PK 필드를 나타냄
- @GeneratedValue : PK의 생성 규칙을 나타냄
- @Column : 테이블의 칼럼을 나타내며 굳이 선언하지 않더라도 해당 클래스의 필드는 모두 칼럼이 됨, 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용
- @NoArgsConstructor : 기본 생성자 자동 추가
- @Getter : 클래스 내 모든 필드의 Getter 메소드를 자동생성
- @Builder : 해당 클래스의 빌더 패턴 클래스를 생성
- 롬복의 어노테이션들은 코드 변경량을 최소화시켜 주기 때문에 적극 사용
- Setter 메소드가 없음
    - 해당 클래스의 인스턴스 값들이 언제 어디서 변해야 하는지 코드상으로 명확하게 구분할 수 없기 때문에
    - Setter가 없어도 생성자를 통해 최종값을 채운 후 DB에 삽입
    - 값 변경이 필요할 경우 해당 이벤트에 맞는 public 메소드를 호출하여 변경하는 것이 전제
    - 이 책은 생성자 대신 @Builder을 통해 제공되는 빌더 클래스 사용

3. PostsRepository 인터페이스 생성

```
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

}
```

- DB Layer 접근자
- JPA에서는 Repository로 부르며 인터페이스로 생성
- JpaRepository<Entity 클래스, PK 타입>를 상속하면 기본적이 CRUD 메소드가 자동 생성
- @Repository 추가 필요 X
- Entity 클래스와 기본 Entity Repository는 함께 위치해야 함

## 3.3 Spring Data JPA 테스트 코드 작성하기

1. test 디렉토리에 domain.posts 패키지 생성 => PostsRepositoryTest 클래스 생성

```
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        *//given*
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("dudrhy12@sookmyung.ac.kr")
                .build());

        *//when*
        List<Posts> postsList = postsRepository.findAll();

        *//then*
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

}
```

- @After
    - JUnit에서 단위 테스트가 끝날 때마다 수행되는 메소드를 지정
    - 보통 배포 전 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용
    - 여러 테스트가 동시에 수행되면 테스트용 데이터베이스인 H2에 데이터가 그대로 남아 있어 다음 테스트 실행 시 테스트가 실패할 수 있음
- @postsRepository.save
    - 테이블 posts에 insert/update 쿼리 실행
    - id 값이 있다면 update가, 없다면 insert 쿼리가 실행
- @postsRepository.findAll
    - 테이블 posts에 있는 모든 데이터 조회

2. src/main/resources/application.properties 생성

```
spring.jpa.show_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
```

## 3.4 등록/수정/조회 API 만들기

- API를 만들기 위해 필요한 클래스 3가지
1. Request 데이터를 받을 Dto
2. API 요청을 받을 Controller
3. 트랜잭션, 도메인 기능 간의 순서를 보장하는 Service
- Spring Web 계층

![https://blog.kakaocdn.net/dn/bk3y8n/btr6Blj6VoL/i8WyhUS4JN8VKumrDvErvK/img.png](https://blog.kakaocdn.net/dn/bk3y8n/btr6Blj6VoL/i8WyhUS4JN8VKumrDvErvK/img.png)

1. Web Layer
    - 흔히 사용하는 컨트롤러(@Controller)와 JSP/Freemarker 등의 뷰 템플릿 영역
    - 이외에도 필터(@Filter), 인터셉터, 컨트롤러 어드바이스(@ControllerAdvice) 등 외부 요청과 응답에 대한 전반적인 영역을 이야기
2. Service Layer
    - @Service에 사용되는 서비스 영역
    - Controller와 Dao 중간 영역에서 일반적으로 사용
    - @Transasctional이 사용되어야 하는 영역이기도
3. Repository Layer
    - Database와 같이 데이터 저장소에 접근하는 영역
4. Dtos
    - 계층 간에 데이터 교환을 위한 객체(Dto)의 영역
    - (Ex) 뷰 템플릿 엔진에서 사용될 객체나 Repository Layer에서 결과로 넘겨준 객체 등
5. Domain Model
    - 도메인이라 불리는 개발 대상을 모든 사람이 동일한 관점에서 이해할 수 있고 공유할 수 있도록 단순화시킨 것
    - (Ex) 택시 앱에서 배차, 탑승, 요금 등이 모두 도메인
    - 데이터베이스의 테이블과 관계가 있어야만 하는 것은 아님
    - VO 같은 값 객체들도 해당
- 트랜잭션 스크립트
    - 기존에 서비스로 처리하던 방식
    - 모든 로직이 서비스 클래스 내부에서 처리
    - 서비스 계층이 무의미
    - 객체가 단순히 데이터 덩어리 역할만
- 그래서 도메인 모델에서 처리
    - 서비스 메소드는 트랜잭션과 도메인 간의 순서만 보장
1. web 패키지 => PostsApiController 클래스 생성

```
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }
}
```

2. com.example.SpringBootAWS 패키지 => service.posts 패키지 생성 => PostsService 클래스 생성

```
import com.example.SpringBootAWS.domain.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }
}
```

- 스프링에서 Bean을 주입 받는 방식
    - @Autowired => 권장 X
    - setter
    - 생성자 => 가장 권장
- @RequiredArgsConstructor에서 final이 선언된 모든 필드를 인자값으로 하는 생성자 생성
- 해당 클래스의 의존성 관계가 변경될 때마다 생성자 코드를 수정할 필요 X

3. web 패키지 => dto 패키지 => PostsSaveRequestDto 클래스 생성

```
@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

}
```

- Entity 클래스를 Request/Response 클래스로 사용 X (Entity 클래스는 데이터베이스와 맞닿은 핵심 클래스)
- View Layer와 DB Layer의 역할 분리 필요
- Entity 클래스와 Controller에서 쓸 Dto 분리 사용

4. test 패키지 => web 패키지 => PostsApiControllerTest 클래스 생성

```
import com.example.SpringBootAWS.domain.posts.Posts;
import com.example.SpringBootAWS.domain.posts.PostsRepository;
import com.example.SpringBootAWS.web.dto.PostsSaveRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.assertj.core.api.Assertions.*;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception {
        *//given*
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        *//when*
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        *//then*
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L));
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }
}
```

- @WebMvcTest의 경우 JPA 기능이 작동하지 않음, 외부 연동과 관련된 부분만 활성화

5. PostApiController 클래스 수정

```
@PostMapping("/api/v1/posts/{id}")
public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
    return postsService.update(id, requestDto);
}

@GetMapping("/api/v1/posts/{id}")
public PostsResponseDto findById(@PathVariable Long id) {
    return postsService.findById(id);
}
```

6. main 패키지 => web 패키지 => dto 패키지 => PostsResponseDto 클래스 생성

```
import com.example.SpringBootAWS.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
```

- Entity의 필드 중 일부만 사용 => 생성자로 Entity를 받아 필드에 값을 넣음, Dto는 Entity를 받아 처리

7. main 패키지 => web 패키지 => dto 패키지 => PostsUpdateRequestDto 클래스 생성

```
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;

    @Builder
    public PostsUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
```

8. Posts.java 수정

```
public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
```

9. PostsService.java 수정

```
@Transactional
public Long update(Long id, PostsUpdateRequestDto requestDto) {
    Posts posts = postsRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

    posts.update(requestDto.getTitle(), requestDto.getContent());

    return id;
}

public PostsResponseDto findById(Long id) {
    Posts entity = postsRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

    return new PostsResponseDto(entity);
}
```

- update 기능에서 데이터베이스에 쿼리를 날리는 부분 X
    - JPA의 영속성 컨텍스트 때문
    - 영속성 컨텍스트란 엔티티를 영구 저장하는 환경
    - JPA의 엔티티 매니저가 활성화된 상태로 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면 이 뎅터는 영속성 컨텍스트가 유지된 상태
    - 해당 데이터의 값 변경 시 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영
    - = 더티 체킹

10. PostsApiControllerTest.java 수정

```
    @Test
    @WithMockUser(roles="USER")
    public void Posts_수정된다() throws Exception {
        *//given*
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        *//when*
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        *//then*
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
}
```

11. PostsApiControllerTest.java 수정

```
    @Test
    *//@WithMockUser(roles = "USER")*public void Posts_수정된다() throws Exception {
        *//given*
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        *//when*
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        *//then*
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
```

12. application.properties에 추가

`spring.h2.console.enabled=true`

13.localhost:8080/h2-console 접속

![https://blog.kakaocdn.net/dn/bewWrh/btr6GqxOaHd/zPVGOuV8ZKliztaZlN2wqK/img.png](https://blog.kakaocdn.net/dn/bewWrh/btr6GqxOaHd/zPVGOuV8ZKliztaZlN2wqK/img.png)

JDBC URL 꼭 동일해야 함

14. Connect 버튼 => POSTS 테이블 정상 노출 확인 => select 쿼리 실행

![https://blog.kakaocdn.net/dn/O6L7z/btr6Bg4kB7L/SeW70FkxRNlC9A9ZLAVMcK/img.png](https://blog.kakaocdn.net/dn/O6L7z/btr6Bg4kB7L/SeW70FkxRNlC9A9ZLAVMcK/img.png)

15. insert 퀴리 실행

![https://blog.kakaocdn.net/dn/bZ4BBQ/btr6K3ChgiO/abRCyOgnayxywudLjzhgJk/img.png](https://blog.kakaocdn.net/dn/bZ4BBQ/btr6K3ChgiO/abRCyOgnayxywudLjzhgJk/img.png)

16. API 요청

![https://blog.kakaocdn.net/dn/KuOA1/btr6Cfw1BQ7/LyRIa9cpyRDgz5v0nuq9d0/img.png](https://blog.kakaocdn.net/dn/KuOA1/btr6Cfw1BQ7/LyRIa9cpyRDgz5v0nuq9d0/img.png)

## 3.5 JPA Auditing으로 생성시간/수정시간 자동화하기

- LocalDate => 날짜 타입 사용

1. domain패키지 => BaseTimeEntity 클래스 생성

```
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    
    @CreatedDate
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
```

- 모든 Entity의 상위 클래스가 되어 Entity들의 createdDate, modifiedDate를 자동으로 관리하는 역할을 가짐
- @MappedSuperclass : JPA Entity 클래스들이 BaseTimeEntity을 상속할 경우 필드들도 칼럼으로 인식하도록
- @EntityListeners(AuditingEntityListener.class) : BaseTimeEntity 클래스에 Auditing 기능 포함
- @CreatedDate : Entity가 생성되어 저장될 때 시간이 자동 저장
- @LastModifiedDate : 조회한 Entity의 값을 변경할 때 시간이 자동 저장

2. Posts 클래스 수정

`public class Posts extends BaseTimeEntity { ... }`

3. Appication 클래스에 어노테이션 추가

`@EnableJpaAuditing`

4. PostsRepositoryTest 클래스 수정

```
 @Test
    public void BaseTimeEntity_등록() {
//given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
//when
        List<Posts> postsList = postsRepository.findAll();

//then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>> createDate=" + posts.getCreatedDate() + ", modifiedDate=" + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
```

- 앞으로 추가될 엔티티들 또한 BaseTimeEntity만 상속받으면 등록일/수정일로 고민할 필요 X
