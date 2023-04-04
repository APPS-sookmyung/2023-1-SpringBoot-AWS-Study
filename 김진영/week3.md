# week 3

## 서버 템플릿 엔진과 머스테치 소개 
### 템플릿 엔진
* **템플릿 엔진**: 웹 개발에 있어, 지정된 템플릿 양식과 데이터가 합쳐져 HTML 문서를 출력하는 소프트웨어 (EX. React, Vue의 View 파일들)
    * **서버 템플릿 엔진**: JSP, Freemarker 등 
        * 화면 생성은 서버에서 **Java 코드로 문자열**을 만든 뒤 이 **문자열을 HTML로 변환**하여 브라우저로 전달 
    * **클라이언트 템플릿 엔진** : Vue.js, React.js (SPA: Single Page Application)
        * SPA는 브라우저에서 화면을 생성함 -> **서버에서 이미 코드가 벗어난 것**! 
        * 서버에서는 Json or Xml 형식의 데이터만 전달하고 클라이언트에서 조립함 
        * 자바스크립트 프레임워크 (리액트, 뷰) 에서 서버 사이드 렌더링을 최근에 지원하고 있음 -> 자바스크립트 프레임워크의 화면 생성 방식을 서버에서 실행하는 것 (많은 수고가 필요하므로 스프링 부트에 대한 이해도와 자바스크립트 프레임워크 양쪽에 대한 이해도가 높아졌을 때 시도해보기!)
### 머스테치 
* **머스테치**: 수많은 언어를 지원하는 가장 심플한 템플릿 엔진 (루비, 자바스크립트, 파이썬, PHP 등 현존하는 대부분 언어 지원)
* 자바 진영에서의 템플릿 엔진(그리고 단점) 
    * JSP, Velocity : 스프링부트에서는 권장하지 않음
    * Freemarker: 템플릿 엔진으로는 너무 과하게 많은 기능 지원함 (높은 자유도)
    * Thymeleaf: 문법이 어려움. Vue.js를 사용해 본 경험이 있어 태그 속성 방식이 익숙하다면 선택해도 됨!
* 머스테치의 장점 
    * 문법이 심플
    * 로직 코드를 사용할 수 없어서 View 역할과 서버의 역할이 명확하게 분리됨
    * Mustache.js와 Mustache.java 2가지가 다 있음 -> 하나의 문법으로 클라이언트/서버 템플릿 모두 사용 가능 
---

## 기본 페이지 만들기 
* 머스테치는 스프링 부트에서 공식 지원하는 템플릿 엔진 -> 의존성 하나만 추가하면 끝!
* url 매핑은 Controller에서 
    ```java  
    @Controller
    public class IndexController {
        @GetMapping("/")
        public String index(){
            return "index"; 
        }
    }
    ```
* 앞의 경로와 뒤의 파일 확장자는 자동으로 지정됨 
    * 앞의 경로: src/main/resources/templates 
    * 뒤의 파일 확장자: .mustache => index 반환 
     => src/main/resources/templates/index.mustache 로 전환되어 View Resolver가 처리하게 됨 
---

## 게시글 등록 화면 만들기 
* 부트스트랩 오픈소스 프론트엔드 라이브러리를 사용.
    * 부트스트랩, 제이쿼리 등 프론트엔드 라이브러리를 사용할 수 있는 방법 
        1. 외부 CDN 사용 -> 강의에서는 이 방법을 채택, 실제 서비스에서는 이 방법을 잘 사용하지 않음 
        2. 직접 라이브러리를 받아서 사용 
* 부트스트랩과 제이쿼리를 index.mustache에 **레이아웃** 방식으로 추가
    * 레이아웃 방식: 공통 영역을 별도의 파일로 분리하여 필요한 곳에서 가져다 쓰는 방식 
* 페이지 로딩 속도 높이기 위해 css는 header, js는 footer에 
    * HTML은 위에서부터 코드가 실행되기 때문에 head가 다 실행되고 나서 body가 실행됨 
    * bootstrap.js의 경우 제이쿼리가 꼭 있어야하므로 부트스트랩보다 먼저 호출되도록 코드 작성 -> bootstrap.js가 제이쿼리에 의존 
* 게시글 등록이 되지 않는 문제 발생 (글이 등록되었습니다 alert가 뜨지 않는 문제)
    * [해결 참고]("https://github.com/jojoldu/freelec-springboot2-webservice/issues/707")
    * 폴더를 생성할때 static.js.app으로 생성하지 않고 static/js/app 이런식으로 하였더니 정상 작동하였다. 

---

## 전체 조회 화면 만들기
* 쿼리 추가
    ```java
    package example.org.domain.posts;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;

    import java.util.List;

    public interface PostsRepository extends JpaRepository<Posts,Long> { //JpaRepository<Entity 클래스, PK타입> -> 기본적인 CRUD 메소드 생성
        @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
        List<Posts> findAllDesc(); 
    }
    ```
    * SpringDataJpa에서 제공하지 않는 메소드는 위처럼 쿼리로 작성해도 됨 
    * 위의 코드는 SpringDataJpa에서 제공하는 기본 메소드만으로 해결 가능, but 가독성이 좋아서 사용한 것 
* Transactional 어노테이션에서 readOnly 속성을 추가하는 것이 안되어서 다음과 같이 글을 참고하여 문제를 해결했다. 속성을 허용하지 않는 Transactional 어노테이션을 사용해서 발생한 문제였다. [해결]("https://velog.io/@be_have98/Spring-Boot-Cannot-resolve-method-readOnly-%EC%98%A4%EB%A5%98")

---
