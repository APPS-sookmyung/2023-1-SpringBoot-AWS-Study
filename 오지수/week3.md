스프링 부트와 AWS로 혼자 구현하는 웹 서비스

# Week 3

## chap04. 머스테치로 화면 구성하기

### 4.1 서버 템플릿 엔진과 머스테치 소개

- 템플릿 엔진? 지정된 템플릿 양식과 데이터가 합쳐져 HTML 문서를 출력하는 소프트웨어!
- 머스테치란?
  - 수많은 언어를 지원하는 가장 심플한 템플릿 엔진.
- 그 외에도 Thymeleaf 템플릿 엔진 존재.

<br>

### 4.2 기본 페이지 만들기

build.gradle 에 머스테치 의존성 추가하기

```gradle
	implementation 'org.springframework.boot:spring-boot-starter-mustache'
```

(코드는 따로 첨부하지 않음.)

<br>

### 4.3 화면 만들기

header.mustache, footer.mustache 파일을 따로 만들어 반복되는 코드를 단축시킴.

- 참고로, js의 용량이 클수록 body 부분의 실행이 늦어지기에 js는 body 하단에 두어 화면이 다 그려진 뒤 호출하는 것이 좋음.

---

#### mustache 문법

<br>

```mustache
{{>layout/header}}
```

- {{> }}는 현재 머스테치 파일을 기준으로 다른 파일을 가져오는 것.

<br>

```mustache
{{#posts}}
```

- posts라는 List를 순회함.

<br>

```mustache
{{id}} 등의 {{변수명}}
```

- List에서 뽑아낸 객체의 필드를 사용

---

#### js 문법

```javascript
window.location.href = "/";
```

- 글 등록이 성공하면 메인페이지('/')로 이동합니다.

<br>

```javascript
$("#btn-update").on("click");
```

- btn-update란 id를 가진 HTML 엘리먼트레 click 이벤트가 발생할 때 update function을 실행하도록 이벤트를 등록.

<br>

```javascript
update: function()
```

- 신규로 추가될 update function입니다.

<br>

```javascript
type: "PUT";
```

- 여러 HTTP Method 중 PUT 메서드 선택하기
- 참고로 REST 에서 CRUD는 다음과 같이 매핑함.
  - 생성 - POST
  - 읽기 - GET
  - 수정 - PUT
  - 삭제 - DELETE

<br>

---

## 과제 - Thymeleaf 문법 알아오기

### Thymeleaf?

스프링 진영에서 밀고 있지만, 문법이 굉장히 어려움.

[공식 문서 이동하기](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#standard-expression-syntax)

<br>

- dependency 추가하기

```gradle
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```

<br>

- HTML 문서 설정

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <title>Title</title>
  </head>
  <body>
    <h1 th:text="${name}">Name</h1>
  </body>
</html>
```

<br>

- 기본 형태

```html
<div th:[속성]="서버에서 받는 값 및 조건식" />
```

<br>

---

#### 문법

```html
<div th:text="${data}"></div>
```

- 컨트롤러에서 전달받은 데이터에 접근하기

<br>

```html
<input type="text" id="userId" th:value="${userId} + '의 이름은 ${userName}" />
```

- input value에 값을 삽입할 때 사용함.
- 여러개의 값을 넣을 땐 + 기호 사용하기.

<br>

```html
<body>
  <a th:hrf="@{/boardListPage?currentPageNum={page}}"></a>
</body>
```

- `<a>`태그의 href 속성과 동일함.

<br>

```html
<body>
  <li
    th:each="pageButton"
    :
    ${#numbers.sequece(paging.firstPage,
    paging.lastPage)}
  ></li>
</body>
```

- 반복문.
  - ${list}로 값을 받아온 것을 변수로 하나씩 가져온다는 뜻. 변수는 이름을 마음대로 지정 가능함.

<br>

```html
<p th:if="${student.grade > 80}">합격입니다!!!</p>
<p th:unless="${student.grade > 80}">불합격.. 좀 더 노력하세요!</p>
```

- 조건문.
  - 유의) else대신 unless를 사용함.
