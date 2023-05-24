# WEEK 7

## CHAPTER 08: EC2 서버에 프로젝트를 배포해보자

---

### 8.1 EC2에 프로젝트 Clone 받기

1. EC2에 깃 설치
    
    `sudo yum install git`
    
2. git clone으로 프로젝트를 저장할 디렉토리 생성
    
    `mkdir ~/app && mkdir ~/app/step1`
    
3. 생성된 디렉토리로 이동
    
    `cd ~/app/step1`
    
4. git clone
    
    `git clone 복사한 주소`
    
5. 코드들이 잘 수행되는지 테스트로 검증
    
    `./gradlew test`
    

---

### 8.2 배포 스크립트 만들기

deploy.sh 작성

```bash
#!/bin/bash

REPOSITORY=/home/ec2-user/app/step1
PROJECT_NAME=springboot-aws

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git Pull"

git pull

echo "> 프로젝트 Build 시작"

./gradlew clean build -x test

echo "> step1 디렉토리로 이동"

cd $REPOSITORY

echo "> Build 파일 복사"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 PID 확인"

CURRENT_PID=${pgrep-f${PROJECT_NAME}.*.jar}

echo "현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다"
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/ |grep jar|tail -n 1)

echo "> JAR Name : $JAR_NAME"

nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &
```

---

### 8.3 외부 Security 파일 등록하기

application-oauth.properties → 깃허브에 안올라와 있으므로 서버에 추가

application-oauth.properties을 쓰도록 deploy.sh 파일 수정

```bash
...
nohup java -jar \
        -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties \
        $REPOSITORY/$JAR_NAME 2>&1 &
...
```

---

### 8.4 스프링 부트 프로젝트로 RDS 접근하기

**RDS 테이블 생성** 

- schema-mysql.sql
    
    ```sql
    CREATE TABLE SPRING_SESSION (
        PRIMARY_ID CHAR(36) NOT NULL,
        SESSION_ID CHAR(36) NOT NULL,
        CREATION_TIME BIGINT NOT NULL,
        LAST_ACCESS_TIME BIGINT NOT NULL,
        MAX_INACTIVE_INTERVAL INT NOT NULL,
        EXPIRY_TIME BIGINT NOT NULL,
        PRINCIPAL_NAME VARCHAR(100),
        CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
    ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
    
    CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
    CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
    CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);
    
    CREATE TABLE SPRING_SESSION_ATTRIBUTES (
        SESSION_PRIMARY_ID CHAR(36) NOT NULL,
        ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
        ATTRIBUTE_BYTES BLOB NOT NULL,
        CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
        CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
    ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
    ```
    
- build.gradle에 MariaDB 드라이버 추가
    
    `implementation 'org.mariadb.jdbc:mariadb-java-client'`
    
- application-real.properties 추가
    
    ```
    spring.profiles.include=oauth,real-db
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLInnoDBDialect
    spring.session.store-type=jdbc
    ```
    

**EC2 설정**

- `vim ~/app/application-real-db.properties`
    
    ```
    spring.jpa.hibernate.ddl-auto=none
    spring.datasource.url=jdbc:mariadb://nari-springboot2-webservice.c2uzhopa8dtr.ap-northeast-2.rds.amazonaws.com:3306/nari_springboot2_webservice
    spring.datasource.username=nari
    spring.datasource.password=sun940114!
    spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
    ```
    
- deploy.sh 수정
    
    ```
    nohup java -jar \
            -Dspring.config.location=optional:/application.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties,optional:/application-real.properties  \
            -Dspring.profiles.active=real \
            $REPOSITORY/$JAR_NAME 2>&1 &
    ```
    
    classpath 대신 optional하니까 갑자기 오류없이 됐어요 ..!
    

---

### 8.5 EC2에서 소셜 로그인하기

aws보안그룹 변경 - 8080추가

aws ec2 도메인으로 접속 - 퍼블릭 DNS(IPV4)+:8080

구글에 EC2 주소 등록

네이버에 ec2 주소 등록

## 과제

vim 사용법

- vim 이란
    - vi (visual editor) : 유닉스, 리눅스에서 가장 많이 사용하는 에디터
    - vim (vi improved) : vi에 추가적 확장 기능을 부여한 에디터
    - 리눅스에서는 vim이 사용되며, 유닉스, 맥OSX, Windows도 지원
- 모드
    - 일반(normal mode) 모드
    - 입력(insert) 모드
    - 명령행(command-line) 모드
- `$ **vim/vi 만들고자하는 파일 이름.파일의 확장자**`
- **입력모드로 전환하기(a 또는 i또는 s또는 o누르기)**
- **명령모드로 전환하기 (ESC 누르기)**
- **저장하기 (:w)**
- **종료하기(:q)**
- **강제종료 (:q!)**
- **저장하면서 종료하기(:wq 또는 :wq!)**
