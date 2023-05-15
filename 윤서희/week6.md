# Week6

## Chap07. AWS에 데이터베이스 환경을 만들어보자 - AWS RDS

- 웹서비스에서의 백엔드
    
    애플리케이션 코드를 작성하는 것만큼 중요한 것이 **데이터베이스**를 다루는 것
    
- 데이터베이스를 구축하고 앞 장에서 만든 EC2 서버와 연동할 예정
    
    ⇒ AWS에서는 관리형 서비스인 **RDS** 제공
    
    - RDS (Relational Database Service)
        
        AWS에서 지원하는 클라우드 기반 관계형 데이터베이스
        

### 7.1 RDS 인스턴스 생성하기

- **MariaDB**를 RDS로 선택한 이유
    - 가격 : 라이센스 비용 영향
    - Amazon Aurora 교체 용이성 : 클라우드 기반에 맞게 재구성한 데이터베이스
        
        ⇒ 엄청난 성능을 제공하고 계속해서 발전하며 다양한 기능을 제공하고 있음
        
        ⇒ Aurora를 선택 안하는 이유 : 프리티어 대상 X, 최저 비용이 월 10만원 이상
        
- MariaDB 장점 (→ MySQL 기반)
    - 동일 하드웨어 사양으로 MySQL보다 향상된 성능
    - 좀 더 활성화된 커뮤니티
    - 다양한 기능과 스토리지 엔진

### 7.2 RDS 운영환경에 맞는 파라미터 설정하기

- 필수 설정들 (파라미터 생성 후 편집)
    - 타임존 : Asia/Seoul
    - Character Set : character → utf8mb4, collation → utf8mb4_general_ci
    - Max Connection → 150

### 7.3 내 PC에서 RDS에 접속해보기

- RDS의 보안 그룹에 본인 PC의 IP 추가
- IntelliJ에 Database 플러그인 설치

- character_set_database, collation_connection : latin1 → 다른 필드와 마찬가지로 utf8mb4로 변경
    ⇒ utf8과 달리 utf8mb4는 이모지 저장할 수 있음

- 쿼리 작성

### 7.4 EC2에서 RDS에서 접근 확인

- putty를 이용해서 EC2에 ssh 접속
    
    `sudo yum install mysql`
    
    ⇒ 충돌이 날 경우 인스턴스 초기 생성 때 변경
    
- RDS 접속
    
    `mysql -u 계정 -p -h Host 주소`
    
    ⇒ Host 주소: 엔드포인트
