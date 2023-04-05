## 머스테치(Mustache)로 화면 구성하기

### [1] 서버 템플릿 엔진과 머스테치 소개

1. 템플릿 엔진 : 지정된 템플릿 양식 + 데이터 -> HTML 문서 출력하는 소프트웨어

   - 서버 템플릿 엔진 : 서버에서 구동
     - ex. JSP, Freemarker
     - 서버에서 Java 코드로 만든 뒤 HTML로 변환하여 브라우저로 전달
   - 클라이언트 템플릿 엔진 : 브라우저 위에서 작동
     - ex. React, Vue
     - 브라우저에서 화면은 생성, 이는 서버에서 이미 코드가 벗어난 경우임
     - 서버에서느 Json 혹은 XML 형식의 데이터만 전달하고 클라이언트에서 조립

2. 머스테치(Mustache) : 수많은 언어를 지원하는 가장 심플한 템플릿 엔진
   - 대부분의 언어 지원(Ruby, JS, Python, PHP, Java 등)
   - 자바에서 사용할 때 -> 서버 템플릿 엔진, JS에서 사용할 때 -> 클라리언트 템플릿 엔진
   - 장점
     - 문법이 심플
     - 로직 코드 사용 X -> View, 서버의 역할이 명확히 분리
     - 하나의 문법으로 클라이언트/서버 템플릿 모두 사용 가능(Mustache.java, Mustache.js)
   - 머스테치 플러그인 설치
     - Settings > Plugins > Handlebars/Mustache 플러그인 설치 -> 재시작

### [2] 기본 페이지 만들기

1. 머스테치 스타터 의존성 등록

   - buld.gradle에 의존성 등록 : `compile('org.springframework.boot:spring-boot-starter-mustache')`
   - 머스테치는 스프링 부트에서 공식 지원하는 템플릿 엔진(의존성만 추가하면 별도 설치 X)

2. index.mustache 파일 생성
   - mustache 파일 위치 : src/main/resources/templates
   - URL 매핑 : Contorller에서 진행
     - IndexController 클래스 생성
   - 머스테치 스타터 : 컨트롤러에서 문자열 반환 시 앞의 경로와 뒤의 파일 확장자는 자동으로 지정됨
     - "index"만 반환 -> src/main/resources/templates(앞 경로), .mustache(뒤 확장자) 자동으로 붙어서 View Resolver가 처리
3. 테스트 코드 검증 : IndexControllerTest 클래스 생성
   - URL 호출 시 페이지의 내용(h1 태그 내용)이 제대로 호출되는 지 검증
   - 코드 설명
     - TestRestTemplate을 통해 "/"로 호출 시 index.mustache에 포함된 코드 있는지 확인
     - "스프링 부트로 시작하는 웹 서비스" 문자열 포함되어 있는지만 검증
4. 실제 화면으로 확인
   - Applicatoin.java의 main 메소드 실행 -> localhost:8080 접속

### [3] 게시글 등록 화면 만들기

1.  부트스트랩, 제이쿼리 라이브러리 사용

    - 레이아웃 방식으로 추가 : 별도의 파일로 분리하여 필요할 떄 import하여 사용(머스테치 파일 어디서나 사용해야 하니)
      - src/main/resources/templates 하위에 layout 디렉토리 추가
      - footer.mustache, header.mustache 파일 생성
    - 외부 CDN 사용
      - 페이지 로딩 속도 높이기 위해 css는 header에, js는 footer에 둠
      - JS는 용량이 클수록 body 부분의 실행이 늦어지기 때문에 body 하단에 두어 화면이 다 그려진 뒤 호출하는 것이 좋음
      - CSS는 화면을 그리는 역할이므로 head에서 불러오는 것이 좋음
      - bootstrap.js응 제이쿼리에 의존하기 때문에 부트스트랩보다 먼저 호출되도록 앞에 작성
    - 기타 HTML 태그들이 모두 레이아웃에 추가되므로 index.mustache에는 필요한 코드만 남게 됨
      - {{>}} : 현재 머스테치 파일을 기준으로 다른 파일 가져옴

2.  글 등록 버튼 추가
    - <a> 태그로 글 등록 페이지로 이동하는 버튼 생성
    - 이동할 페이지 주소 : `href="/posts/save"` 부분
3.  주소(/posts/save)에 해당하는 컨트롤러 생성(IndexController 안에)
4.  posts-save.mustache 파일 생성
5.  실제 화면으로 확인

6.  등록 버튼 기능 추가
    - API 호출하는 JS 생성
      - src/main/resources 하위에 static/js/app 디렉토리 생성
      - index.js 생성
        - `window.location.href = '/';` : 글 등록 성공 시 메인페이지로 이동
        - index 변수의 속성으로 function 추가
          - 만약 js 파일마다 고유의 init과 save function이 있다면?
            - 브라우저의 스코프는 공용 공간으로 쓰이기 때문에 나중에 로딩된 js의 init, save가 먼저 로딩된 js의 function을 덮어쓰게 됨
          - 중복된 함수 이름은 자주 발생하기 때문에 덮어쓰게 되는 문제르 ㄹ피하기 위해 var index 객체 생성하여 그 안에 필요한 모든 function을 생성 -> index 객체 안에서만 function 유효하여 겹칠 위험이 사라짐
    - index.js footer.mustache 파일에 추가
      - `<script src="/js/app/index.js"></script>`
      - src/main/resources/static에 위치한 정적 파일들은 URL에서 /로 설정

### [4] 전체 조회 화면 만들기

1. index.mustache UI 변경
   - 머스테치 문법 사용
     - {{#posts}} : posts라는 list 순회(for문)
     - {{변수명}} : 객체의 필드 사용
2. PostsRepository 인터페이스에 쿼리 추가
   - @Query 사용 : SpringDataJpa에서 제공하지 않는 메소드는 쿼리로 작성
3. "po. 코드 추가

- postsRepository 결과로 넘어온 Posts의 stream을 map을 통해 PostsListResponceDto로 변환 -> List로 반환
- `@Transactional(readOnly = true)` : 트랜잭션 범위는 유효하되, 조회 기능만 남겨둠
  - 조회 속도가 개선됨 -> 등록, 수정, 삭제 기능이 전혀 없는 서비스 메소드에서 사용 추천
- `.map(PostsListResponceDto::new)` == `.map(posts -> new PostsListResponceDto(posts))`

4. PostsListResponceDto 클래스 생성
5. PostsApiController 변경
   - Model
     - 서버 템플릿 엔진에서 사용할 수 있는 객체 저장 가능
     - postsService.finAllDesc()로 가져온 결과를 "posts" index.mustache에 전달

### [5] 게시글 수정, 삭제 화면 만들기

#### 1. 게시글 수정

1. 게시글 수정 화면 머스테치 생성 : posts-update.mustache
   - {{post.id}} : 객체 필드 .으로 접근(Post 클래스의 id 접근)
2. 수정 기능 호출할 수 있게 index.js 파일에 update function 추가
   - `type: 'PUT'` : HTTP 메소드 중 put 메소드 선택
     - PostsApiController에서 이미 @PutMapping으로 선언했기 때문에 PUT 사용해야 함
     - REST 규약 : Create-POST / Read-GET / Update-PUT / Delete-DELETE
3. 전체 목록 -> 수정 페이지로 이동 기능 추가
   - title에 <a> 태그 추가
4. Controller 코드 작업

#### 2. 게시글 삭제

1. posts-update.mustache에 삭제 버튼 추가
2. index.js 파일에 delete function 추가
   - `type: 'DELETE'`
3. postsService에 삭제 API 생성
   - 존재하는 Posts인지 확인 -> 조회 후 삭제
   - `postsRepository.delete(posts);` : JpaRepository에서 지원하는 delete 메소드
     - delete(Entity) or deleteById(id)로 삭제 가능
4. delete 메소드를 Controller가 사용하도록 코드 추가

### [6] Thymeleaf 문법 조사

Thymeleaf : HTML, CSS, JS, XML 및 일반 텍스트를 처리할 수 있는 웹 및 독립 실행형 환경 모두를 위한 최신 서버 측 Java 템플릿 엔진(-thymeleaf 공식 문서)

1. 템플릿 모드 : 6가지 종류의 템플릿을 처리할 수 있으며 각 템플릿을 '템플릿 모드'라고 함

   - 템플릿 HTML모드 : 모든 종류의 HTML 입력을 허용
   - 템플릿 XML모드 : XML 입력을 허용
   - 템플릿 TEXT모드 : 비마크업 특성의 템플릿에 대한 특수 구문 사용 가능, 단순한 텍스트로 처리
   - 템플릿 JAVASCRIPT모드 : Thymeleaf 애플리케이션에서 JavaScript 파일 처리 가능
   - 템플릿 CSS모드 : Thymeleaf 애플리케이션과 관련된 CSS 파일을 처리 가능
   - 템플릿 RAW모드 : 템플릿을 전혀 처리 X, 처리 중인 템플릿에 손대지 않은 리소스(파일, URL 응답 등)를 삽입하는 데 사용

2. 기본 Syntax : html태그에 th문법을 추가하는 형태
   표현식 : `<tagName th:[속성]=”서버에서 받는 값 및 조건식”/>`

   - `th:text` : 태그의 텍스트를 서버에서 전달 받은 값으로 대치
     - `<p th:text="#{home.welcome}">Welcome to our grocery store!</p>`
   - `th:utext` : HTML 태그를 존중하고 이스케이프하지 않도록 함

     ```
     home.welcome=Welcome to our <b>fantastic</b> grocery store!

     <p th:utext="#{home.welcome}">Welcome to our grocery store!</p> -> <p>Welcome to our <b>fantastic</b> grocery store!</p>
     ```

   - `th:value` : value 속성을 설정
     - `<input type="submit" value="Subscribe!" th:value="#{subscribe.submit}"/>`
   - `th:with` : 변수를 선언하여 사용
     - `<div th:with=”temp=${hello}” th:text=”${temp}”>`
   - `th:switch & th:case` : Switch-case문, default case는 \*로 처리

   ```
   <div th:switch="${user.role}">
       <p th:case="'admin'">User is an administrator</p>
       <p th:case="#{roles.manager}">User is a manager</p>
       <p th:case="*">User is some other thing</p>
   </div>
   ```

   - `th:if & th:unless` : 조건문(if-unless)
     - `<div th:if="${user.isAdmin()} == false">`
   - `th:each` : 반복문(Iteration)

   ```
   <tr th:each="prod : ${prods}">
       <td th:text="${prod.name}">Onions</td>
       <td th:text="${prod.price}">2.41</td>
       <td th:text="${prod.inStock}? #{true} : #{false}">yes</td>
   </tr>
   ```
