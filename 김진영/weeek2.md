# week 2

## JPA 소개 
### JPA
* 객체를 관계형 데이터 베이스(RDB; Relational Database)에서 관리하는 것이 중요 
* **SQL은 피할 수 없다!**
-> 단순 반복 작업.. 문제 발생 
* 문제 : 패러다임 불일치 문제 
    * RDB: 어떻게 데이터를 저장할지에 초점이 맞춰진 기술
    * 객체지향 프로그래밍 언어: 메시지를 기반으로 기능과 속성을 한 곳에서 관리하는 기술 
    * -> 관계형 데이터베이스와 객체지향 프로그래밍 언어의 패러다임이 서로 다른데, 객체를 데이터베이스에 저장하려고 하니 여러 문제가 발생하는 것 
    * 객체 모델링을 데이터베이스로 구현할 수 없기 때문에 웹 애플리케이션 개발이 점점 **데이터베이스 모델링**에만 집중하게 됨. 
        -> `JPA` 등장 
            * 서로 지향하는 바가 다른 2개 영역(객체지향 프로그래밍 언어, 관계형 데이터베이스)을 **중간에서 패러다임 일치**를 시켜주기 위한 기술 
* 개발자는 **객체지향적으로 프로그래밍**을 하고, JPA가 관계형 데이터베이스에 맞게 SQL을 대신 생성해서 실행함. 
    * 개발자는 더이상 **SQL에 종속적인 개발을 하지 않아도 됨**
    * 유지 보수가 편함! 

### Spring Data JPA
* JPA는 인터페이스로서 자바 표준 명세서 
    * 인터페이스인 JPA 사용하기 위해 구현체 필요 
    * 구현체 좀 더 쉽게 사용하고자 추상화시킨 Spring Data JPA라는 모듈을 사용하여 JPA 기술 다룸 
* **Spring Data JPA** 
    * 구현체 교체의 용이성: Hibernate 외에 다른 구현체로 쉽게 교체하기 위함 
    * 저장소 교체의 용이성: 관계형 데이터베이스 외에 다른 저장소로 쉽게 교체하기 위함 

## 프로젝트에 Spring Data Jpa 적용하기 
```java
package example.org.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter //Lombok 어노테이션 , 클래스 내 모든 필드의 Getter 메소드를 자동 생성 
@NoArgsConstructor //Lombok 어노테이션 , 기본 생성자 자동 추가 
@Entity //JPA 어노테이션 , 테이블과 링크될 클래스임을 나타냄 
public class Posts { //실제 DB의 테이블과 매칭될 클래스 (=Entity 클래스) 
    @Id //PK필드 
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 생성 규칙 
    private Long id;

    //테이블의 칼럼. 굳이 선언하지 않아도 해당 클래스의 필드는 모두 컬럼이 됨 
    //기본 값 이외의 추가로 변경이 필요한 옵션이 있으면 사용함 
    @Column(length = 500,nullable = false) 
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    private String author; 
    
    @Builder //해당 클래스의 빌더 패턴 클래스를 생성 
    public Posts(String title, String content, String author){
        this.title=title;
        this.content=content;
        this.author=author; 
    }
}
```
* Entity 클래스에서는 절대 Setter 메소드를 만들지 않음! (해당 클래스의 인스턴스 값들이 언제 어디서 변해야하는지 코드상으로 명확하게 구분할 수가 없음, 차후 기능 변경 시 복잡해지므로)
* 빌더 사용 시 **어느 필드에 어떤 값을 채워야하는지 명확하게 인지가능**
```java
public interface PostsRepository extends JpaRepository<Posts,Long> { 
    //JpaRepository<Entity 클래스, PK타입> -> 기본적인 CRUD 메소드 생성 
}
```
* @Repository 추가할 필요 없음 
* 주의! Entity 클래스와 기본 Entity Repository는 함께 위치해야함 
    * Entity 클래스는 기본 Repository 없이는 제대로 역할 할 수 없음 

## Spring Data JPA 테스트 코드 작성 
```java
package example.org.web.domain.posts;

import example.org.domain.posts.Posts;
import example.org.domain.posts.PostsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest //별다른 설정 없이 H2 데이터베이스 자동으로 실행
public class PostsRepositoryTest {
    @Autowired
    PostsRepository postsRepository;

    @After //JUnit에서 단위 테스트가 끝날 때마다 수행되는 메소드를 지정
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){
        //given
        String title="테스트 게시글";
        String content="테스트 본문";

        postsRepository.save(Posts.builder() //postsRepository.save 는 테이블 posts에 insert/update 쿼리를 실행. (id 있으면 update, 없으면 insert)
                .title(title)
                .content(content)
                .author("jinyoung@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll(); //테이블 posts에 있는 모든 데이터를 조회해오는 메소드

        //then
        Posts posts=postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }
}
```

## 등록/수정/조회 API 만들기
* API 만들기 위해 필요한 3개의 클래스
    1. Request 데이터를 받을 Dto
    2. API 요청을 받을 Controller
    3. 트랜잭션, 도메인 기능 간의 순서를 보장하는 Service 
* 영역
    1. Web Layer 
        * 흔히 사용하는 컨트롤러와 JSP/Freemaker 등의 뷰 템플릿 영역 
        * 외부 요청과 응답에 대한 전반적인 영역
    2. Service Layer
        * 서비스 영역
        * 일반적으로 Controller와 Dao의 중간 영역에서 사용됨 
        * @Transactional이 사용되어야 하는 영역이기도 함 
    3. RepositoryLayer
        * Database와 같이 데이터 저장소에 접근하는 영역
    4. Dtos
        * Dto(Data Transfer Object)는 계층 간에 데이터 교환을 위한 객체 
    5. Domain Model
        * 도메인이라 불리는 개발 대상을 모든 사람이 동일한 관점에서 이해할 수 있고 공유할 수 있도록 단순화시킨 것
        * 무조건 데이터베이스의 테이블과 관계가 있어야하는 것은 아님 
* Domain에서 비지니스 처리 담당해야 함 
* Controller 와 Service에서 @Autowired 대신 생성자로 Bean 객체 받도록 하면 @Autowired와 동일한 효과 보일 수 있음 
    * 생성자는 @RequiredArgsConstructor에서 해결해줌 (final이 선언된 모든 필드를 인자값으로 하는 생성자를 @RequiredArgsConsructor가 대신 생성해줌)
* JPA의 영속성 컨텍스트 : 엔티티를 영구 저장하는 환경

### 과제 
* Posts에 Delete 기능 추가 
    * Controller에 DeleteMapping 사용하여 path variable로 id 받음 
        ```java
            //PostsApiController.java 
            @DeleteMapping("/api/v1/posts/{id}")
            public void delete (@PathVariable Long id){
                postsService.delete(id);
            }
        ```
    * transactional delete 함수 구현 
        ```java
            @Transactional
            public void delete(Long id){
                Posts posts=postsRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
                postsRepository.delete(posts);
            }
        ```
    * Test 코드 
        ```java
         @Test
        public void Posts_삭제된다() throws Exception{
            //given
            Posts savedPosts=postsRepository.save(Posts.builder()
                    .title("title")
                    .content("content")
                    .author("author")
                    .build());
            Long deleteId= savedPosts.getId();

            String url="http://localhost:"+port+"/api/v1/posts/"+deleteId;


            //when
            ResponseEntity<Void> responseEntity=restTemplate.exchange(url,HttpMethod.DELETE,HttpEntity.EMPTY,Void.class);

            //then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);


            List<Posts> all=postsRepository.findAll();
            assertThat(all).isEmpty();

        }
        ```
    