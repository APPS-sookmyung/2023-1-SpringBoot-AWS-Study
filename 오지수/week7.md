# EC2 서버에 스프링 부트 프로젝트 배포하는 방법

## git으로 EC2에 프로젝트를 clone 하기

참고

- `./gradlew build` 이 명령어는.. test 가 전부 성공하면 실행하도록 한다. <br>
  실패하면 무한로딩 + 굉장히 오래 걸림!
- `./gradlew clean build -x test` 이렇게 주어야 테스트 제외 빠르게 실행 가능.

### deploy.sh 작성하기

```bash
#!/bin/bash

REPOSITORY=/home/ec2-user/app/step1/2023-1-SpringBoot-AWS-Study
PROJECT_NAME=springboot-aws

cd $REPOSITORY/오지수/$PROJECT_NAME/

echo "> GIT Pull"

git pull


echo "> 프로젝트 Build 시작"

./gradlew clean build -x test

echo "> step 1 디렉토리로 이동"

cd $REPOSITORY

echo "> BUILD 파일 복사"

cp $REPOSITORY/오지수/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/오지수/

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID = $(pgrep -f ${PROJECT_NAME}.*.jar)

echo "> 현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID"  ]; then
        echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/오지수/ | grep jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

nohup java -jar \
      -Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-oauth.properties \
      $REPOSITORY/오지수/$JAR_NAME 2>&1 &

nohup java -jar \
-Dspring.config.location=classpath:/application.yml, /home/ec2-user/app/application-oauth.properties, /home/ec2-user/app/application-real-db.properties, classpath:/application-real.properties \
-Dspring.profiles.active=real \
$REPOSITORY/오지수/$JAR_NAME 2>&1 &

```

## 숨겨야 하는 정보는 서버가 아예 갖고 있게 하기

application-oauth.properties 파일을 생성하여 로컬에 있는 파일 내용을 옮기기

### 스프링 세션 테이블 RDS에 적용

```sql
CREATE TABLE SPRING_SESSION
(
    PRIMARY_ID            CHAR(36) NOT NULL,
    SESSION_ID            CHAR(36) NOT NULL,
    CREATION_TIME         BIGINT   NOT NULL,
    LAST_ACCESS_TIME      BIGINT   NOT NULL,
    MAX_INACTIVE_INTERVAL INT      NOT NULL,
    EXPIRY_TIME           BIGINT   NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME     VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES    BLOB         NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

```

### posts, user table 반영하기

```sql
create table posts
(
    id            bigint       not null auto_increment,
    created_date  datetime,
    modified_date datetime,
    author        varchar(255),
    content       TEXT         not null,
    title         varchar(500) not null,
    primary key (id)
) engine = InnoDB;
create table user
(
    id            bigint       not null auto_increment,
    created_date  datetime,
    modified_date datetime,
    email         varchar(255) not null,
    name          varchar(255) not null,
    picture       varchar(255),
    role          varchar(255) not null,
    primary key (id)
) engine = InnoDB;
```

이후 mariaDB도 연결했으나.. error....
이후 실습 진행 불가..
=> 버전 이슈인 것 같으나 레퍼런스 부족 + 너무 달라진 환경으로 에러 발생 원인 찾기 어려움..

---

# 과제

vi는 모든 유닉스/리눅스 시스템에서 사용할 수 있는 텍스트 에디터. 이의 향상된 버전이 vim이다.

vi 에디터의 가장 큰 특징은 명령 모드와 입력 모드가 구분되어 있다는 점.

- 명령 모드: 다양한 vi 명령어를 사용할 수 있는 상태.
  (처음 실행할 때 명령모드로 시작함) - 입력하는 모든 것이 vi 명령어로 해석됨.
- 입력 모드: 텍스트를 입력할 수 있는 상태.
  - 텍스트를 입력하고 싶다면? 명령 모드에서 텍스트를 입력하고자 하는 곳으로 커서를 옮긴 후 입력 모드 전환 명령어를 이용하여 입력 모드로 전환하고 텍스트 입력 후에 `ESC` 키를 이용하여 다시 명령 모드로 되돌아옴.

## 원하는 위치로 이동하는 명령어

### 명령어 목록

- 커서 이동
  - ←, h \*\*\*\*: 한 칸 왼쪽으로 이동
  - ↓, j : 한 칸 아래쪽으로 이동
  - ↑, k : 한 칸 위쪽으로 이동
  - →,l : 한 칸 오른쪽으로 이동
  - BACKSPACE : 왼쪽으로 한 칸 이동
  - SPACE : 오른쪽으로 한 칸 이동
  - - : 이전 줄의 처음으로 이동
  - +, RETURN : 다음 줄의 처음으로 이동
  - 0 : 현재 줄의 맨 앞으로 이동
  - $ : 현재 줄의 맨 끝으로 이동
  - ^ : 현재 줄의 첫 글자로 이동
  - w : 다음 단어의 첫 글자로 이동
  - b : 이전 단어의 첫 글자로 이동

---

- 화면 이동
  - ^F : 한 화면 아래로 이동
  - ^B : 한 화면 위로 이동
  - ^D : 반 화면 아래로 이동
  - ^U : 반 화면 위로 이동

---

- 특정 줄로 이동
  - nG : n번째 줄로 이동
  - G : 마지막 줄로 이동
  - :n : n번째 줄로 이동

---

- 탐색
  - / 또는 ? 기호를 사용하여 단어나 문장을 찾을 수 있음.
    - /탐색패턴 : 커서 이후에 대해 검색을 수행하여 해당 위치로 커서 이동
    - ?탐색패턴 : 커서 이전에 대해 검색을 수행하여 해당 위치로 커서 이동

## 입력 모드로 전환하는 명령어

### 명령어 목록

- i : 커서 위치 앞에 삽입 (insert)
- a : 커서 위치 뒤에 삽입 (append)
- I : 현재 줄의 앞에 삽입 (Insert)
- A : 현재 줄의 뒤에 삽입 (Append)
- o : 현재 줄의 아래에 전개
  - vim에서는 enter키도 가능한듯함.
- O : 현재 줄의 위에 전개

## 파일에 저장 후 끝내기

### 파일에 저장하기

- :w : 현재 파일에 저장한다.
- :w 파일이름 : 지정된 파일에 저장한다.

### 파일에 저장하고 끝내기

- :wq : 현재 파일에 저장하고 종료한다.
- ZZ : 현재 파일에 저장하고 종료한다.

### 저장하지 않고 끝내기

- :q : 아무런 작업을 하지 않은 경우의 종료
- :q! : **(위험!!)** 작업 내용을 저장하지 않고 종료

## 그 외

### 다른 파일 편집하기

현재 편집하고 있던 파일 대신에, 다른 파일을 불러서 편집을 수행하기

- :e 파일이름 : 현재 파일 대신에, 주어진 파일을 연다.
- :e# : 편집했던 이전 파일을 다시 연다.

### 줄 번호 붙이기

- :set number | :set nu : 줄번호 붙이기
- :set nonumber | :set non : 줄번호 없애기
