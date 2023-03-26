# Chap01

## 1.1 인텔리제이 소개

- 인텔리제이의 강점
1. 강력한 추천 기능 (Smart Completion)
2. 훨씬 더 다양한 리팩토링과 디버깅 기능
3. 이클립스의 깃(Git)에 비해 훨씬 높은 자유도
4. 프로젝트 시작할 때 인덱싱을 하여 파일을 비롯한 자원들에 대한 빠른 검색 속도
5. HTML, CSS, JS, XML에 대한 강력한 기능 지원
6. 자바, 스프링 부트 버전업에 맞춘 빠른 업데이트
- 인텔리제이 커뮤니티(무료)의 기능
1. 자바 개발에 대한 모든 기능 지원
2. Maven, Gradle과 같은 빌드 도구 기능 지원
3. 깃 & 깃허브와 같은 VCS(버전 관리 시스템) 기능 지원
4. 스프링 부트의 경우 톰캣과 같은 별도의 외장 서버 없이 실행 가능

## 1.2 인텔리제이 설치하기

- 젯브레인 : 제품 전체를 관리해주는 데스크톱 앱, 모든 제품군의 버전 관리와 JVM 옵션 등 조정 가능
    - [JetBrains: 소프트웨어 개발자 및 팀을 위한 필수 도구](https://www.jetbrains.com/ko-kr/)
- 인텔리제이
    - [IntelliJ IDEA – Java 및 Kotlin을 위한 최고의 IDE (jetbrains.com)](https://www.jetbrains.com/ko-kr/idea/?utm_source=naver&utm_medium=cpc&utm_campaign=KR-Naver-PR-IntelliJ-PC&utm_content=KR-PR-IntelliJ-Extended&utm_term=%EC%9D%B8%ED%85%94%EB%A6%AC%EC%A0%9C%EC%9D%B4%EB%8B%A4%EC%9A%B4%EB%A1%9C%EB%93%9C)

## 1.3 인텔리제이 커뮤니티에서 프로젝트 생성하기

- 인텔리제이에는 프로젝트와 모듈의 개념만 있음
- 인텔리제이는 모든 프로젝트를 한 번에 불러올 수 없음, 한 화면에서는 하나의 프로젝트만 오픈
- 설정
    - 프로젝트 유형 : Gradle

## 1.4 그레이들 프로젝트를 스프링 부트 프로젝트로 변경하기

- build.gradle 파일 수정
- 물론 '스프링 이니셜라이저'로 설정 가능
- 플로그인 의존성 관리를 위한 설정

```
buildscript{
    ext{
springBootVersion = '2.1.9.RELEASE'
}
repositories{
	mavenCentral()
        jcenter()
}
dependencies{
classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
}
}
```

1. ext : buid.gradle 에서 사용하는 전역변수를 설정하겠다는 의미
2. springBootVersion : 전역 변수를 생성하고 그 값을 ‘2.1.9.RELEASE’로 하겠다는 의미
3. = spring-boot-gradle-plugin이라는 스프링 부트 그레이들 플로그인의 2.1.7.RELEASE를 의존성으로 받겠다
- 플로그인 의존성들을 적용할 것인지 결정

```
apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'
```

1. io.spring.dependency-management : 스프링 부트의 의존성들을 관리해 주는 플러그인으로 필수 추가
2. 앞 4개의 플러그인 : 자바와 스프링 부트를 사용하기 위한 필수 플러그
- 이외 코드

```
repositories {
    mavenCentral()
    jcenter()
}
```

1. repositories : 각종 의존성 (라이브러리)들을 어떤 원격 저장소에서 받을지 결정, 기본적으로 mavenCentral을 많이 사용하지만 최근에는 라이브러리 업로드 난이도 때문에 jcenter도 많이 사용
2. mavenCentral : 라이브러리 업로드를 위해 많은 과정과 설정 필요
3. jcenter : 라이브러리 업로드를 간단히 함, mavenCentral에도 업로드 될 수 있도록 자동화

```
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web')
    testImplementation('org.springframework.boot:spring-boot-starter-test')

    implementation('org.projectlombok:lombok')
}
```

1. dependencies : 프로젝트 개발에 필요한 의존성들 선언
2. 인텔리제이는 메이븐 저장소의 데이터를 인덱싱해서 관리하기 때문에 커뮤니티 버전을 사용해도 의존성 자동완성이 가능
3. 의존성 코드는 특정 버전을 명시하지 말아야 함, 그래야 가장 위의 "org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}"의 버전을 따라가게 됨
- 전체코드

```
buildscript{
    ext{
        springBootVersion='2.1.7.RELEASE'
    }
    repositories{
        mavenCentral()
        jcenter()
    }
    dependencies{
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

plugins {
    id 'java'
}

group 'org.example'
version '1.0-SNAPSHOT'
sourceCompatibility=1.8

apply plugin:'java'
apply plugin:'eclipse'
apply plugin:'org.springframework.boot'
apply plugin:'io.spring.dependency-management'

repositories {
    mavenCentral()
}

dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web')
    testImplementation('org.springframework.boot:spring-boot-starter-test')

    implementation('org.projectlombok:lombok')
}

test {
    useJUnitPlatform()
}
```

## 1.5 인텔리제이에서 깃과 깃허브 사용하기

- .idea 디렉토리는 커밋 X
1. 플러그인 목록에서 .ignore 설치 후 재시
2. .gitignore 파일에 코드 추가
