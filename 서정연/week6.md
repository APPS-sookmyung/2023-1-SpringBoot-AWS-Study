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
