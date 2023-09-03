## Chapter 7 : AWS에 데이터베이스 환경 만들기 - AWS RDS

<br>
-> 데이터베이스 구축 후 EC2 서버와 연결
- AWS에서는 관리형 서비스인 RDS(Relational Database Service) 제공
- RDS : AWS에서 지원하는 클라우드 기반 관계형 데이터베이스

### [1] RDS 인스턴스 생성하기

- RDS 대시보드 > 데이터베이스 생성 클릭
- DB 엔진은 MariaDB 선택
  - MySQL, MariaDB, PostgreSQL 중에 선택하길 추천
  - 가격 효율성, Amazon Aurora 교체 용이성 장점을 가짐
  - MySQL 기반으로 만들어졌기 때문에 쿼리를 비롯한 전반적인 사용법은 MySQL과 유사
- 사용 사례로 프리티어 선택
- 할당된 스토리지는 20GB 설정
- DB 인스턴스와 마스터 사용자 정보 등록
- 네트워크 퍼블릭 액세스 허용

### [2] RDS 운영환경에 맞는 파라미터 설정하기

1. 파라미터 설정하기

- 타임존
  - 파라미터 그룹 > 파라미터 그룹 생성 > MariaDB와 같은 버전 선택
  - 생성된 파라미터 그룹을 편집 모드로 전환하여 타입존을 Asia/Seoul로 변경
- Character Set
  - character 항목 -> utf8mb4
  - collation 항목 -> utf8mb4_general_ci
  - utf8과 utf8mb4의 차이는 이모지 저장 가능 여부(순서대로 불가능, 가능)
- Max Connection
  - RDS Max Connection 값 150으로 늘려줌

2. 설정된 파라미터 그룹 데이터베이스에 연결

- DB 파라미터 그룹 default -> 생성한 신규 파라미터 그룹으로 변경 후 저장
- 반영 시전 즉시 적용으로 선택

3. 바뀐 옵션 정상 적용을 위해 재부팅 진행

### [3] 내 PC에서 RDS에 접속해보기

1. RDS 보안그룹에 본인 PC의 IP 추가

- RDS 세부 정보 페이지 > 보안 그룹
- EC2에서 사용된 보안그룹과 내 IP를 RDS 보안 그룹 인바운드로 추가

2. 로컬에서 테스트

- 인텔리제이에서 Database Navigator 플러그인 설치
- Action 검색(Command + Shift + a)으로 데이터베이스 브라우저 실행
- MySQL 접속 후 RDS 정보 차례로 등록
- open SQL console > new SQL console > 새로 생성될 콘솔창 이름 저장(cindy-springboot-webservice)
- 생성된 콘솔창에서 SQL 실행 : `use AWS의 RDS 데이터베이스명;`
- 쿼리문 선택 후 Execute Statement 버튼 클릭
- 데이터베이스가 선택된 상태에서 현재의 character_set, collation 설정을 확인 : `show variables like 'c%'`
- latin 1으로 설정된 collation_database, character_set_database utf8mb4로 직접 변경 :

```
ALTER DATABASE 데이터베이스명
CHARACTER SET = 'utf8mb4'
COLLATE = 'utf8mb4_general_ci'
```

- 타임존 확인 : `select @@time_zone, now();`
- 한글명 잘 들어가는지 간단한 테이블 생성과 insert 쿼리 실행(쿼리문 마다 실행)

```
CREATE TABLE test(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  content varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

insert into test(content) values ('테스트');

select * from test;
```

### [4] EC2에서 RDS 접근 확인

- mysql 설치

```
sudo su

yum localinstall https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm

yum install mysql-community-server

systemctl start mysqld

systemctl status mysqld
```

- RDS 접속 : `mysql -u cindy -p -h 데이터베이스 엔드포인트` > 비밀번호 입력
- 데이터베이스 확인 : `show databases;`
