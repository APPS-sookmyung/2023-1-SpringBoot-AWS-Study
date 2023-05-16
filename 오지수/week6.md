스프링 부트와 AWS로 혼자 구현하는 웹 서비스

# Week 6
## Chapter07. AWS에 데이터베이스 환경을 만들어보자 - AWS RDS
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
<img width="223" alt="image" src="https://github.com/5jisoo/2023-1-SpringBoot-AWS-Study/assets/96935231/ebfd4b4c-5434-49a6-9373-b52eb2b96b0d">


### EC2에서 RDS에서 접근 확인
