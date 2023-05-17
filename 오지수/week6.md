스프링 부트와 AWS로 혼자 구현하는 웹 서비스

# Week 6
## Chapter07. AWS에 데이터베이스 환경을 만들어보자 - AWS RDS

### RDS 인스턴스 생성하기
MariaDB를 선택한 이유?
- 가격
- Amazon Aurora 교체 용이성
    -  Amazon Aurora: AWS에서 MySQL와 PostgreSQL을 클라우드 기반에 맞게 재구성한 서비스

MySQL 대비 장점
- 동일 하드웨어 사양으로 보다 향상된 성능
- 좀 더 활성화된 커뮤니티
- 다양한 기능
- 다양한 스토리지 엔진

### 내 PC에서 RDS와 접속해보기
- PC에서 접속 후 한글 데이터 등록 확인하기
```sql
create TABLE test(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    content varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
)ENGINE = InnoDB;

insert into test(content) values('테스트');

select * from test;
```
-> 테스트 완료! <br>
<img width="300" alt="image" src="https://github.com/5jisoo/2023-1-SpringBoot-AWS-Study/assets/96935231/ebfd4b4c-5434-49a6-9373-b52eb2b96b0d">


### EC2에서 RDS에서 접근 확인
[MySQL 설치 과정 참고](https://goddaehee.tistory.com/292)

---
### 과제
1. 실습 완료 사진 <br>
 <img width="300" alt="image" src="https://github.com/5jisoo/2023-1-SpringBoot-AWS-Study/assets/96935231/a72a8e7a-efa8-4aeb-b056-73cf021f4bf8">

<br><br>

2. 구축된 RDS에 `User` 테이블을 하나 생성하고(속성은 ID, 이름, 이메일로 설정함.) SELECT * FROM User 쿼리를 하나 날려봅시다! <br>
```sql
create TABLE User(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    email varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
)ENGINE = InnoDB;

insert into User(name, email) values('lucy', 'lucy27@sookmyung.ac.kr');
select * from User;
```
<img width="500" alt="image" src="https://github.com/5jisoo/2023-1-SpringBoot-AWS-Study/assets/96935231/d4c4b805-7fae-4b9e-a707-d93f1f6f90bc">

<br><br>

3. User 테이블 삭제 쿼리
```sql
drop TABLE User;
```
<img width="700" alt="image" src="https://github.com/5jisoo/2023-1-SpringBoot-AWS-Study/assets/96935231/db55e138-82cd-4f7e-8692-cd0946acc020">


