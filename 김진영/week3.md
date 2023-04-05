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

## 게시글 수정,삭제 화면 만들기
* {{posts.id}}
    * 머스테치는 객체의 필드 접근 시 점으로 구분함
    * Post 클래스의 id에 대한 접근은 post.id 
* readonly
    * input 태그에 읽기 가능만 허용하는 속성
    * id와 author은 수정할 수 없도록 읽기만 허용하도록 추가 

---

## 과제 
### ThymeLeaf 문법 조사 
* 타임리프(Thymeleaf)란? 
    * view template engine
    * 서버에서 클라이언트에게 응답할 브라우저 화면을 만들어주는 역할
    * 목표: 템플릿을 만들 때 유지관리가 쉽도록 하는 것 
        * 그러기 위해 `Natural Templates`를 기반으로 함 (기존 HTML 코드와 구조를 변경하지 않고 덧붙이는 방식)
    * 장점
        1. 코드를 변경하지 않기 때문에 디자인팀과 개발팀간의 협업이 편해짐
        2. JSP와 달리 Servlet Code로 변환되지 않기 때문에 비즈니스 로직과 분리되어 오로지 View에 집중할 수 있음 
        3. 서버상에서 동작하지 않아도 되기 때문에 서버 동작 없이 화면 확인 가능! -> 더미 데이터 놓고 화면 디자인 및 테스트에 용이 
* 기본 문법 
    1. 설정
        * xmlns:th=" "
            ```html
            <html lang="en" xmlns:th="http://www.thymeleaf.org">
            ```
            * 타임리프의 th속성을 사용하기 위해 선언된 네임스테이스
            * 순수 HTML로 이루어진 페이지의 경우 선언하지 않아도 됨
    2. 기본 기능 
        * th:text="${}"
            ```html
            <div th:text="${data}"></div>
            ```
            * ${}를 통해 컨트롤러에서 전달받은 데이터에 접근가능
        * th:href="@{}"
            ```html
            <body>
            <a th:hrf="@{/boardListPage?currentPageNum={page}}"></a>
            </body>
            ```
            * 괄호안에 클릭시 이동하고자하는 url을 입력하면됨
        * th:with="${}"
            ```html
            <div th:with=”userId=${number}” th:text=”${usesrId}”>
            ```
            * 변수형태의 값을 재정의하는 속성. th:with을 이용하여 새로운 변수값을 생성 가능 
        * th:value="${}"
            ```html
            <input type="text" id="userId" th:value="${userId} + '의 이름은 ${userName}"/>
            ```
            * input의 value에 값을 삽입할 때 사용
            * 여러 개의 값을 넣을 땐 + 기호 사용
    3. Layout
        * xmlns:layout="", layout:decorator=""
            ```html
            <html lang="ko" xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout" layout:decorate="~{board/layout/basic}">
            ```
            * layout 기능 사용하기 위해 의존성 추가 필요
            * xmlns:layout은 타임리프의 레이아웃을 사용하기 위한 선언. 레이아웃을 적용시킬 HTML 파일에 해당 선언을 함 
            * 그리고 해당 페이지에 th:fragment로 조각한 공통 영역을 가져와서 삽입
        * th:block
            ```html
            <html lagn="ko" 
            xmlns:th="http://www.thymeleaf.org"
            xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
            <body>
                //전체 레이아웃
                <th:block th:fragment="footerFragment">
                </th:block>
            </body>  
            
            </html>
            ```
            * block은 타임리프 표현을 어느 곳에서든 사용할 수 있도록 하는 구문
            * 동적인 처리가 필요할 때 주로 사용됨 (layout기능이나 switch에서 자주 사용)
        * th:fragment=""
            ```html
            <body>

            <footer th:fragment="footerFragment">
            <p>안녕하세요</p>
            </footer>

            </body>
            ```
            * 웹페이지에 메뉴 탭이나 네비게이션바와 같이 공통으로 반복되는 영역이 존재하는데, 이런 반복되는 공통 영역을 fragment가 정의하여 코드를 정리해줌 
            * 특히 header와 footer에 삽입하여 조각화함. 
                * 조각을 삽입하고자 하는 대상 HTML 파일에서 th:replace"[파일경로::조각이름]" 을 통해 삽입함
        * th:replace="~{파일경로::조각이름}"
            ```html
            <body>
                <div th:replace="~{/common/footer :: footerFragment}"></div>  
            </body>
            ```
            * fragment로 조각화한 공통 영역을 HTML에 삽입하는 역할 
            * :: 을 기준으로 앞에는 조각이 있는 경로, 뒤에는 조각의 이름 
        * th:insert="~{파일경로::조각이름}"
            ```html
            <body>
                <div th:insert="~{/common/footer :: footerFragment}"></div>  
            </body>
            ```
            * insert는 태그 내로 조각을 삽입하는 방법. replace는 완전하게 대체하기 때문에 replace 태그가 입력된 <div>가 사라지고 fragment로 조각화한 코드가 완전히 대체됨
            * 그러나 insert는 insert가 입력된 <div> 안에 fragment를 삽입하는 개념이기 때문에 <div> 안에 조각화된 코드가 삽입됨
    4. form
        ```html
        <body>
            <form th:action="@{/join}" th:object="${joinForm}" method="post">
                <input type="text" id="userId" th:field="*{userId}" >
                <input type="password" id="userPw" th:field="*{userPw}" >
            </form>
        </body>
        ```
        * th:action="@{}"
            * <form>태그 사용시 해당경로로 요청을 보낼 때 사용
        * th:objet="${}"
            * <form>태그에서 데이터 보내기 위해 submit할 때 데이터가 th:object 속성을 통해 object에 지정한 객체에 값을 담아 넘김. 이때 값을 th:field 속성과 함께 사용하여 넘김. 
            * controller와 view 사이의 dto 클래스 객체라고 생각하자..
        * th:field="*{}"
            * th:object 속성과 함께 th:field를 이용하여 HTML 태그에 멤버 변수 매핑가능
            * th:field를 이용한 사용자 입력 필드는 id,name,value속성 값이 자동으로 매핑됨
            * th:object, th:field는 controller에서 특정 클래스의 객체를 전달받은 경우에만 사용 가능
    5. 조건문과 반복문
        * th:if="${}", th:unless="${}"
            ```html
            <span th:if="${userNum} == 1"></span> 
            <span th:unless="${userNum} == 2"></span>
            ```
            * 각각 if와 else에 해당
            * th:unless는 일반적인 언어의 else문과 달리 th:if에 들어가는 조건과 동일한 조건을 지정해야 
        * th:each="변수:${list}"
            ```html
            <body>
                <li th:each="pageButton" : ${#numbers.sequece(paging.firstPage, paging.lastPage)}></li>
            </body>
            ```
            * for문
            * ${list}에서 받아온 것을 변수로 하나씩 가져옴. 
        * th:switch, th:case
            ```html
                <th:block th:switch="${userNum}"> 
                    <span th:case="1">권한1</span> 
                    <span th:case="2">권한2</span> 
                </th:block>
            ```
            * switch-case문 
            * switch case문으로 제어할 태그를 th:block으로 설정하고 그 안에 코드 작성 
            