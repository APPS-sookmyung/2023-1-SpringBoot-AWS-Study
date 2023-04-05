# Week3
## Chap 04. 머스테치로 화면 구성하기
### 1. 서버 템플릿 엔진과 머스테치 소개
#### 템플릿 엔진
지정된 템플릿 양식과 데이터가 합쳐져 HTML 문서를 출력하는 소프트웨어
* 서버 템플릿 엔진: 서버에서 구동
* 서버에서 Java 코드로 문자열 생성 -> 문자열을 HTML로 변환 -> 브라우저로 전달
#### 머스테치란
수많은 언어를 지원하는 가장 심플한 템플릿 엔진

### 2. 기본 페이지 만들기
* build.gradle: 머스테치 스타터 의존성 등록
#### IndexController (src/main/java/web)
* Controller에서 URL 매핑
* 머스테치 스타터로 인해 문자열을 반환할 때 앞의 경로와 뒤의 파일 확장자는 자동으로 지정 (src/main/resources/templates/index.mustache 로 전달되어 View Resolver가 처리)
#### IndexControllerTest
* TestRestTemplate을 통해 "/"로 호출했을 때 index.mustache에 포함된 코드들이 있는지 확인
* GetMapping은 IndexController에서!
### 3. 게시글 등록 화면 만들기
* 프론트 엔드 라이브러리 사용하기
   * 라이브러리 부트 스트랩과 제이쿼리 -> index.mustache에 레이아웃 방식으로 추가
   * css는 header에, js는 footer에 설정 (페이지 로딩 속도를 높이기 위함)
 #### IndexController
* /posts/save 매핑해서 index.mustache와 마찬가지로 /posts/save를 호출하면 posts-save/mustache를 호출하는 메소드 추가 -> posts-save.mustache 파일 생성
#### posts-save.mustache
#### index.js
* 중복된 함수이름을 피하기 위해 index.js만의 유효범위를 생성
* index 객체 안에서만 function이 유효 -> 다른 JS와 겹칠 위험이 사라짐
#### PostsRepository
쿼리 추가
#### PostsService
* findAllDesc 메소드에 @Transactional 추가
* postRepository 결과로 넘어온 Posts의 Stram을 PostsListResponseDto 변환 -> List로 반환
### 5. 게시글 수정, 삭제 화면 만들기
#### posts-update.mustache (게시글 수정 머스테치)
* REST에서 CRUD는 다음과 같이 HTTP Method에 매핑
   * 생성 - POST
   * 읽기 - GET
   * 수정 - PUT
   * 삭제 - DELETE

## 3주차 과제
### 타임리프란
* 클라이언트에게 응답할 브라우저 화면을 만들어주는 역할
* 기존 HTML 코드와 구조를 변경하지 않고 덧붙여 템플릿을 만들 때 유지관리하기 쉽도록 함

### 기본 설정
#### 의존성 추가
* Maven
```` html
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
* Gradle
``` java
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
* 타임리프를 적용할 HTML 문서에 네임 스페이스 추가
``` html
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

### 기본 문법
#### 기본 기능
* th:text="${}" : 컨트롤러에서 전달받은 데이터에 접근
* th:href="@{}" : 클릭시 이동하고자 하는 url 입력하면 이동
* th:with="${}" : 변수 형태의 값을 재정의, 새로운 변수값을 새성할 수 있음
* th:value="${}" : input의 value에 값을 삽입할 때 사용, + 기호를 사용하여 여러 값을 넣을 수 있음
#### 조건문과 반복문
* th:if="${}", th:unless="${}" : if와 else에 해당, 동일한 조건을 지정해야 함
* th:each="변수 : ${list}" : for문을 의미
* th:switch, th:case : switch-case문과 동일
