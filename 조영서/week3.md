# Chap04

## 4.1 서버 템플릿 엔진과 머스테치 소개

### 템플릿 엔진

- 지정된 템플릿 양식과 데이터가 합쳐져 HTML 문서를 출력하는 소프트웨어
- 서버 템플릿 엔진
    - JSP, Freemarker
    - 서버에서 구동
    - 서버에서 Java 코드로 문자열 생성 ⇒ HTML로 변환 ⇒ 브라우저 전달
- 클라이언트 템플릿 엔진
    - React, Vue의 View 파일들
    - 브라우저에서 화면 생성
    - 서버에서 이미 코드가 벗어난 경우
    - Json 혹은 Xml 형식의 데이터만 전달 ⇒ 클라이언트에서 조립
- 자바스크립트
    - 브라우저 위에서 작동

### 머스테치

- 수많은 언어를 지원하는 가장 심플한 템플릿 엔진
- HTML을 만들어주는 템플릿 엔진
- 장점
    - 심플한 문법
    - 로직 코드를 사용할 수 없어 View의 역할과 서버의 역할이 명확하게 분리
    - 클라이언트/서버 템플릿 모두 사용 가능
- 설치
    - Ctrl+Shift+A ⇒ Actions에서 plugins 검색 ⇒ Marketplace에서 mastache 검색 후 설치

## 4.2 기본페이지 만들기

1. build.gradle에 의존성 등록
    
    `implementation('org.springframework.boot:spring-boot-starter-mustache')`
    
2. src/main/resources/templates/index.mustache 생성

```
<!doctype html>
<html>
<head>
    <title>스프링부터 웹 서비스</title>
    <meta http-equiv="Content-Type" content="text/htnl;charest=UTF-8" />
</head>
<body>
<h1>스프링 부트로 시작하는 웹 서비스</h1>h1>
</body>
</html>
```

1. src/main/java/com/example/SpringBootAWS/web/IndexController.java 생성

```
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}

```

- 머스테치에 URL 매핑
- 컨트롤러에서 문자열을 반환할 때 앞의 경로와 뒤의 파일 확장자는 자동으로 지정
    - 앞의 경로 : src/main/resources/templates
    - 뒤의 파일 확장자 : .mustache
    - src/main/resources/templates/index.mustache로 전환되어 View Resolver가 처리
1. src/test/java/com/example/SpringBootAWS/web/IndexControllerTest.java 생성

```
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 메인페이지_로딩() {
        //when
        String body = this.restTemplate.getForObject("/", String.class);

        //then
assertThat(body).contains("스프링부트로 시작하는 웹 서비스");
    }
}
```

- “/”로 호출했을 때 index.mustache에 포함된 코드들ㅇ ㅣ있는지 확인
    - 그 중 “스프링 부트로 시작하는 웹 서비스” 문자열이 포함되어 있는지만 비교

## 4.3 게시글 등록 화면 만들기

- 프론트엔드 랑리브러리 사용
    - 외부 CDN 사용
        - 레이아웃 방식
            - 공통 영역을 별도의 파일로 분리하여 필요한 곳에서 가져다 쓰는 방식
    - 직접 라이브러리를 받아서 사용
1. src/main/resources/templates/layout/header.mustache 생성

```
<!DOCTYPE HTML>
<html>
<head>
    <title>스프링부트 웹서비스</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
```

1. src/main/resources/templates/layout/footer.mustache 생성

```
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>
```

- css는 hearder에 js는 footer에 둠
    - 페이지 로딩속도를 높이기 위해
    - js는 용량이 크면 클수록 body 부분 실행이 늦어지기 때문에 body 하단에 두기
    - css는 화면을 그리는 역할이기에 head에서 부르기
- bootstrap.js
    - 제이쿼리가 꼭 있어야 해서 부트스랩보다 먼저 호출되도록 코드 작성
    - bootstrap.js가 제이쿼리에 의존
- 이제 index.mustacheㅔ는 필요한 고드만 남게 됨
1. index.mustache 수정

```
{{>layout/header}}
<h1>스프링부트로 시작하는 웹 서비스 Ver.2</h1>
<div class="col-md-12">
    <div class="row">
        <div class="col-md-6">
            <a href="/posts/save" role="button" class="btn btn-primary">글 등록</a>
            </div>
    </div>
</div>
{{>layout/footer}}
```

- {{> }} : 현재 머스테치 파일을 기준으로 다른 파일을 가져 옴
- <a> 태그 : 글 등록 페이지로 이동하는 글 등록 버튼 생성 (/posts/save로)
1. [IndexController.java](http://IndexController.java) 수정

```
@GetMapping("/posts/save")
public String postsSave() {
    return "posts-save";
}
```

1. src/main/resources/templates/posts-save.mustache 생성

```
{{>layout/header}}

<h1>게시글 등록</h1>

<div class="col-md-12">
    <div class="col-md-4">
        <form>
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" class="form-control" id="title" placeholder="제목을 입력하세요">
            </div>
            <div class="form-group">
                <label for="author"> 작성자 </label>
                <input type="text" class="form-control" id="author" placeholder="작성자를 입력하세요">
            </div>
            <div class="form-group">
                <label for="content"> 내용 </label>
                <textarea class="form-control" id="content" placeholder="내용을 입력하세요"></textarea>
            </div>
        </form>
        <a href="/" role="button" class="btn btn-secondary">취소</a>
        <button type="button" class="btn btn-primary" id="btn-save">등록</button>
    </div>
</div>

{{>layout/footer}}
```

1. src/main/resources/static/js/app/index.js 생성

```
var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });

        $('#btn-update').on('click', function () {
            _this.update();
        });

        $('#btn-delete').on('click', function () {
            _this.delete();
        });
    },
    save : function () {
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

main.init();
```

- window.location.href = ‘/’ : 글 등록이 성공하면 메인페이지(/)로 이동
1. footer.mustache에 추가

```
<!--index.js 추가-->
<script src="/js/app/index.js"></script>
```

- 절대경로(/)로 바로 시작
