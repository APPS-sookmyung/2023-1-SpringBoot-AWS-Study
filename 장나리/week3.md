# WEEK 3

## CHAPTER 04: 머스테치로 화면 구성하기

머스테치를 통해 화면 영역을 개발하는 방법

서버 템플릿 엔진과 클라이언트 템플릿 엔진의 차이는 무엇인지

왜 JSP가 아닌 머스테치를 선택했는지!

머스테치를 통해 기본적인 CRUD 화면 개발 방법 등

---

### 4.1 서버 템플릿 엔진과 머스테치 소개

1. 템플릿 엔진이란 ?
    1. 지정된 템플릿 양식과 데이터가 합쳐져 HTML 문서를 출력하는 소프트웨어
    2. JSP, Freemarker 등 : 서버 템플릿 엔진 - 서버에서 구동됨
        1. 서버에서 Java 코드로 문자열을 만든 뒤, 이 문자열을 HTML로 변환하여 브라우저로 전달
    3. 리액브, 뷰 등 : 클라이언트 템플릿 엔진
        1. 브라우저에서 화면 생성
        2. 서버에서 이미 코드가 벗어난 경우
2. 머스테치란?
    1. 수많은 언어를 지원하는 가장 심플한 템플릿 엔진
    2. 루비, 자바스크립트, 파이썬, PHP, 자바, 펄 등 현존하는 대부분의 언어 지원
    3. 자바에서 사용될 때는 서버 템플릿 엔진으로, 자바스크립트에서 사용될 때는 클라이언트 템플릿 엔진으로 사용 가능
    4. 머스테치의 장점
        1. 문법이 다른 템플릿 엔진보다 심플
        2. 로직 코드를 사용할 수 없어 View의 역할과 서버의 역할이 명확하게 분리됨
        3. Mustache.js와 Mustache.java 2가지가 다 있어, 하나의 문법으로 클라이언트/서버 템플릿을 모두 사용 가능
3. 머스테치 플러그인 설치
    1. settings → Plugins → mustache검색 → install

---

### 4.2 기본 페이지 만들기

1. build.gradle 등록
    
    `implementation 'org.springframework.boot:spring-boot-starter-mustache'`
    
2. src/main/resource/templates/index.mustache
    
    ```html
    <!DOCTYPE HTML>
    <html>
    <head>
    	<title>스프링부트 웹 서비스 Ver.2</title>
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
    	<h1>스프링 부트로 시작하는 웹서비스</h1>
    </body>
    </html>
    ```
    
3. web 패키지 안에 IndexController 생성
    
    ```java
    @Controller
    public class IndexController {
    
        @GetMapping("/")
        public String index(){
            return "index";
        }
    }
    ```
    
4. test 패키지에 IndexControllerTest 클래스 생성 
    
    ```java
    @SpringBootTest(webEnvironment = RANDOM_PORT)
    public class IndexControllerTest {
        @Autowired
        private TestRestTemplate restTemplate;
    
        @Test
        public void 메인페이지_로딩(){
            //when
            String body = this.restTemplate.getForObject("/",String.class);
            //then
            assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    
        }
    
    }
    ```
    
5. 한글 깨짐 !!!!
    
     구글링하니까 스프링부트 버전 내리면 된다고 했는데, 그러면 코드를 다 고쳐야 해서 **application.properties** 파일에서 `server.servlet.encoding.force=*true`* 추가해 줬습니다!
    

---

### 4.3 게시글 등록 화면 만들기

- 이미 3장에서 API는 만들어 놓음
- 부트스트랩을 이용하여 화면 만들기 → 2개의 라이브러리와 제이쿼리를 index.mustache에 추가 → 레이아웃 방식 사용
- 레이아웃 방식 : 공통 영역을 별도의 파일로 분리하여 필요한 곳에서 가져다 쓰는 방식
1. 부트스트랩과 제이쿼리는 머스테치 화면 어디에서나 필요 → 레이아웃 파일을 만들어 추가(footer.mustache, header.mustache)
    - js는 body 하단에 두어 화면이 다 그려진 뒤에 호출하는 것이 좋음
    - css는 화면을 그리는 역할이므로 head에서 불러오는 것이 좋음
2. index.mustache에는 필요한 코드만 남게됨.
    - {{>layout/header}} : {{> }}는 현재 머스테치 파일(index.mustache)을 기준으로 다른 파일을 가져옴
3. 레이아웃으로 파일을 분리했으니, index.mustache에 글 등록 버튼 추가
    - 여기에서는 `<a>` 태그를 이용해 글 등록 페이지로 이동하는 글 등록 버튼 생성
    - 이동할 주소의 페이지는 /posts/save
4. /posts/save에 해당하는 컨트롤러 생성
    - 페이지와 관련된 컨트롤러는 모두 IndexController
    - index.mustache와 마찬가지로 /posts/save를 호출하면 posts-save.mustache 호출하는 메소드 추가
        
        ```java
            @GetMapping("/posts/save")
            public String postsSave(){
                return "posts-save";
            }
        ```
5. posts-sav.mustache 파일 생성
6. 아직 등록버튼은 기능이 없음 : API를 호출하는 JS가 전혀 없기 때문 → src/main/resource 에 static/js/app 디렉토리 생성
7. 여기에 index.js 생성
    - window.location.href = '/'; : 글 등록이 성공하면 메인페이지(/)로 이동
8. index.js를 머스테치 파일이 쓸 수 있게 footer.mustache에 추가 `<script *src*="/js/app/index.js"></script>`

---

### 4.4 전체 조회 화면 만들기

1. 전체 조회를 위해 index.mustache의 UI 변경
    - {{#posts}}
        - posts라는 List를 순회
        - Java의 for문과 동일
    - {{id}} 등의 {{변수명}}
        - List에서 뽑아낸 객체의 필드 사용
2. PostsRepository 인터페이스에 쿼리 추가
    
    ```java
    public interface PostsRepository extends JpaRepository<Posts,Long> {
    
        @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
        List<Posts> findAllDesc();
    }
    ```
    
3. PostsService
    - findAllDesc 메소드의 트랜잭션 어노테이션(@Transactional)에 옵션이 한 추가
    - readOnly = true를 주면 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선됨 → 등록, 수정, 삭제 기능이 전혀 없는 서비스 메소드에서 사용하는 것을 추천
    - `.map(PostsListResponseDto::new)` = `.map(posts → new PostsListResponseDto(posts))`
        - postsRepository 결과로 넘어온 Posts의 Stream을 map을 통해 PostsListResponseDto 변환 → List로 반환하는 메소드
4. PostsListResponseDto 생성
5. Controller 변경
    
    ```java
    @RequiredArgsConstructor
    @Controller
    public class IndexController {
    
        private final PostsService postsService;
    
        @GetMapping("/")
        public String index(Model model){
            model.addAttribute("posts",postsService.findAllDesc());
            return "index";
        }
    }
    ```
    
    - Model
        - 서버 템플릿 엔진에서 사용할 수 있는 객체 저장 가능
        - 여기서는 postsService.findAllDesc()로 가져온 결과를 posts로 index.mustache에 전달

---

### 4.5 게시글 수정, 삭제 화면 만들기

수정 API는 3.4절에서 만들어 놓음

**게시글 수정**

1. 게시글 수정 화면 머스테치 파일 생성(posts-update.mustache) 
    - {{post.id}}
        - 머스테치는 객체의 필드 접근 시 점(Dot)으로 구분
        - 즉, Post 클래스의 id에 대한 접근은 post.id로 사용할 수 있음
    - readonly
        - input 태그에 읽기 가능만 허용하는 속성
        - id와 author는 수정할 수 없도록 읽기만 허용하도록 추가
2. btn-update버튼을 클릭하면 update 기능을 호출할 수 있게 index.js파일에도 update function 하나 추가
    - $('#btn-update').on('click')
        - btn-update란 id를 가진 HTML 엘리먼트에 click 이벤트가 발생할 때 update function을 실행하도록 이벤트 등록
    - update : function()
        - 신규로 추가될 update function
    - type: ‘PUT’
        - 여러 HTTP Method 중 PUT 메소드 선택
        - PostsApiController에 있는 API에서 이미 @PutMapping으로 선언했기 때문에 PUT을 사용해야 함. 참고로 이는 REST 규약에 맞게 설정된 것
    - url: ‘/api/v1/posts/’+id
        - 어느 게시글에서 수정할 지 URL Path로 구분하기 위해 Path에 id 추가
3. 수정 페이지로 이동할 수 있게 페이지 이동 기능 추가 - index.mustache 코드 수정
    - <a href="/posts/update/{{id}}"></a>
        - 타이틀에 a tag 추가
        - 타이틀을 클릭하면 해당 게시글의 수정 화면으로 이동
4. IndexController 수정

```java
...
public class IndexController {
		...
    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post",dto);

        return "posts-update";
    }
}
```

**게시글 삭제**

1. 수정화면에 추가 
2. 삭제 이벤트 js코드 추가
3. 삭제 API(PostsService)
    
    ```java
    @Transactional
    public void delete(Long id){
            Posts posts = postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
    
            postsRepository.delete(posts);
    }
    ```
    
    - postsRepository.delete(posts)
        - JpaRepository에서 이미 delete메소드를 지우너하고 있으니 이를 활용
        - 엔티티를 파라미터로 삭제할 수도 있고, deleteById 메소드를 이용하면 id로 삭제할 수도 있음
        - 존재하는 Posts인지 확인을 위해 엔티티 조회 후 그대로 삭제
4. delete메소드를 컨트롤러가 사용하도록 코드 추가
    
    ```java
    @DeleteMapping("/api/v1/posts/{id}")
        public Long delete(@PathVariable Long id){
            postsService.delete(id);
            return id;
    }
    ```
    

---

## 과제

### Thymeleaf(타임리프) 문법 조사 하기

- 타임리프란 ?
    - 뷰 템플린 엔진으로 JSP, Freemarkerd와 같이 서버에서 클라이언트에게 응답할 브라우저 화면을 만들어주는 역할
- 타임리프의 장점
    - 코드를 변경하지 않기 때문에 디자인 팀과 개발 팀간의 협업이 편해짐.
    - JSP와 달리 Servlet Code로 변환되지 않기 때문에 비즈니스 로직과 분리되어 오로지 View에 집중 가능
    - 서버상에서 동작하지 않아도 되기 때문에 서버 동작 없이 화면을 확인할 수 있다. 때문에 더미 데이터를 넣고 화면 디자인 및 테스트에 용이
- 타임리프 기본 설정
    1. 의존성 추가
        
        `implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'`
        
    2. 타임리프를 적용할 HTML문서에 네임스페이스 추가
        
        ```java
        <!DOCTYPE html>
        <html lang="en" xmlns:th="http://www.thymeleaf.org">
        <head>
            <meta charset="UTF-8">
            <title>Title</title>
        </head>
        <body>
            <h1 th:text="${name}">Name</h1>
        </body>
        </html>
        ```
        
- 타임리프 기본 문법
    1. 설정
        - xmlns:th=” “
            
            `<html lang="en" xmlns:th="http://www.thymeleaf.org">` 
            
            - 타임리프의 th속성을 사용하기 위해 선언된 네임스페이스
            - 순수 HTML로만 이루어진 페이지의 경우 선언하지 않아도 됨
    2. 기본기능
        - th:text=”${}”
            
            ```java
            <div th:text="${data}"></div>
            ```
            
            - JSP의 EL 표현식인 ${}와 마찬가지로 ${} 표현식을 사용해서 컨트롤러에서 전달받은 데이터에 접근 가능
        - th:href="@{}"
            
            ```java
            <body>
              <a th:hrf="@{/boardListPage?currentPageNum={page}}"></a>
            </body>
            ```
            
            - **`<a>`**태그의 href 속성과 동일
            - 괄호안에 클릭시 이동하고자하는 url 입력
        - th:with="${}"
            
            ```java
            <div th:with=”userId=${number}” th:text=”${usesrId}”>
            ```
            
            - 변수형태의 값을 재정의하는 속성이다.
            - 즉, **`th:with`**를 이용하여 새로운 변수값 생성
        - th:value="${}"
            
            ```java
            <input type="text" id="userId" th:value="${userId} + '의 이름은 ${userName}"/>
            ```
            
            - input의 value에 값을 삽입할 때 사용.
            - 여러개의 값을 넣을땐 + 기호 사용
    3. 조건문과 반복문
        - th:if="${}", th:unless="${}"
            
            ```java
            <span th:if="${userNum} == 1"></span> 
            <span th:unless="${userNum} == 2"></span>
            ```
            
            - JAVA의 조건문에 해당하는 속성
            - 각각 **`if`**와 **`else`**를 뜻함
            - **`th:unless`**는 일반적인 언어의 else 문과는 달리 **`th:if`**에 들어가는 조건과 동일한 조건을 지정해야 함.
        - th:each="변수 : ${list}"
            
            ```java
            <body>
              <li th:each="pageButton" : ${#numbers.sequece(paging.firstPage, paging.lastPage)}></li>
            </body>
            ```
            
            - JSP의 JSTL에서 **`<c:foreach>`** 그리고 JAVA의 반복문 중 **`for문`**을 뜻함
            - • ${list}로 값을 받아온 것을 변수로 하나씩 가져온다는 뜻으로, 변수는 이름을 마음대로 지정 가능
        - th:switch, th:case
            
            ```java
            <th:block th:switch="${userNum}"> 
              <span th:case="1">권한1</span> 
              <span th:case="2">권한2</span> 
            </th:block>
            ```
            
            - JAVA의 **`switch-case`**문과 동일
            - switch case문으로 제어할 태그를 **`th:block`**으로 설정하고 그 안에 코드 작성
            - userNum라는 변수의 값이 1이거나 2일때 동작하는 예제