# WEEK 6

## CHAPTER 07: AWS에 데이터베이스 환경을 만들어보자 - AWS RDS

- 백엔드에서 데이터베이스를 다루는 것은 매우 중요!
- 직접 데이터베이스를 설치하지 않음 → 모니터링, 알람, 백업 모두 직접 해야함
- AWS에서는 RDS(Relational Database Service) 제공
    - RDS : 클라우드 기반형 관계형 데이터베이스
    - 여러 작업을 자동화화여 개발자가 개발에 집중할 수 있도록 도움
    - 조정 가능한 용량을 지원하여 예상치 못한 양의 데이터가 쌓여도 비용만 추가로 내면 정상적으로 서비스 가능

---

### 7.1 RDS 인스턴스 생성하기

- DB엔진 선택
    - MySQL, MariaDB, PostgreSQL 중에 선택하는 것을 추천
    - 그중에서도 MariaDB
        - 가격
        - Amazon Aurora(오로라) 교체 용이성
            - Amazon Aurora는 AWS에서 MySQL과 PostgreSQL을 클라우드 기반에 맞게 재구성한 데이터베이스
            - RDS MySQL 대비 5배, RDS PostgreSQL보다 3배의 성능 제공
            - AWS에서 직접 엔지니어링 하고 있기 때문에 계속해서 발전 중
            - 규모가 일정 이상 커진 후에 Maria DB에서 Aurora로 이전하면 됨
        - MySQL은 가장 인기 있음, 근데 썬마이크로시스템즈와 오라클이 합병하면서 개발자들이 나가 MariaDB 개발
        - MySQL 기반이기 때문에 쿼리를 비롯한 전반적인 사용법이 유사
        - MySQL 대비 장점
            - 동일 하드웨어 사양으로 MySQL보다 향상된 성능
            - 좀 더 활성화된 커뮤니티
            - 다양한 기능
            - 다양한 스토리지 엔진

---

### 7.2 RDS 운영환경에 맞는 파라미터 설정하기

1. RDS 처음 생성하면 필수 설정
    1. 타임존 → Asia/Seoul
    2. Character Set
    3. Max Connection → 150
2. 파라미터 그룹을 데이터 베이스에 연결
    
    즉시 적용하기!!!
    
3. 반영이 안되면 재부팅하기!!

---

### 7.3 내 PC에서 RDS 접속해보기

RDS의 보안 그룹에 본인 PC의 IP 추가하기

1. EC2에 사용된 보안 그룹 ID와 IP를 RDS 보안그룹에서 추가
    1. MySQL/Aurora 선택하면 자동으로 3306 포트 선택됨
    2. 보안그룹의 첫번째 줄 : 현재 내 PC의 IP 등록
    3. 보안그룹의 두번째 줄 : EC2의 보안그룹 추가
2. DataBase 플러그인 설치
    
    인텔리제이에 이미 있음
    
    - RDS 접속 정보 등록
        - Host : RDS의 엔드 포인트 등록
3. 콘솔 사용해보기
    - *`use* nari_springboot2_webservice;` : 쿼리가 수행 될 database 선택
    - *`show variables like* 'c%';` : 현재의 character_set, collation 설정 확인
    - character_set_database, collation_connection 2가지 변경해주기
        
        ```sql
        ALTER DATABASE nari_springboot2_webservice
        CHARACTER SET = 'utf8mb4'
        COLLATE = 'utf8mb4_general_ci';
        ```
        
    - *`select* @@time_zone, *now*();` : 타임존 확인
    - 한글명이 잘 들어가는지 확인
        
        ```sql
        CREATE TABLE test (
            id bigint(20) NOT NULL AUTO_INCREMENT,
            content varchar(255) DEFAULT NULL,
            PRIMARY KEY (id)
        )ENGINE = InnoDB;
        
        insert into test(content) values ('테스트');
        
        select * from test;
        ```
        

---

### 7.4 EC2에서 RDS 접근 확인

mySQL 설치

1. ssh 접속
2. `sudo su`
3. `yum localinstall [https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm](https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm)`
4. `yum install mysql-community-server`
5. `systemctl start mysqld`
6. `systemctl status mysqld`

`mysql -u 계정 -p -h Host 주소` : 접속

`show databases;` : 데이터베이스 목록 확인
