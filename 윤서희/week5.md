# Week5

## Chap06. AWS 서버 환경을 만들어보자 - AWS EC2

- 클라우드 서비스
    
    인터넷(클라우드)을 통해 서버, 스토리지,데이터베이스, 네트워크, 소프트웨어, 모니터링 등의 컴퓨팅 서비스를 제공하는 것
    
    ⇒ AWS가 지원
    
- 클라우드 종류
    1. Infrastructure as a Service
    2. Platform as a Service
    3. Software as a Service

### 6.1 AWS 회원 가입

### 6.2 EC2 인스턴스 생성하기

- EC2 (Elastic Compute Cloud)
    
    AWS에서 제공하는 성능, 용량 등을 유동적으로 사용할 수 있는 서버
    
- Centos AMI 가 아닌 **Linux AMI**를 사용하는 이유
    - 아마존이 개발하고 있기 때문에 지원받기 쉽다.
    - 레드햇 베이스
    - AWS의 각종 서비스와의 상성이 좋다.
    - Amazon 독자적인 개발 리포지터리를 사용하고 있어 yum이 매우 빠르다.
- 인스턴스 유형
    - 크레딧 : CPU를 사용할 수 있는 포인트 개념
    - 인스턴스 크기에 따라 정해진 비율로 CPU 크레딧을 계속 받게 된다.
        
        ⇒ 크레딧이 모두 사용되면 더이상 EC2를 사용할 수 없다.
        

<aside>
🌟 생성한 탄력적 IP는 무조건 EC2에 바로 연결해야 한다.
⇒ 바로 연결하지 않을 경우 비용 발생

</aside>

### 6.3 EC2 서버에 접속하기

- Windows
    
    별도의 클라이언트 설치 ⇒ putty
    
- putty
    
    pem 키 ⇒ ppk 파일로 변환을 해야 사용 가능
    

### 6.4 아마존 리눅스 1 서버 생성 시 꼭 해야할 설정들

- Java 8 설치
- 타임존 변경 : 한국 시간대로
- 호스트 네임 변경
---
## 5주차 과제

### 클라우드 컴퓨팅
- 컴퓨팅 리소스를 인터넷을 통해 서비스로 사용할 수 있는 주문형 서비스
- 기업에서 직접 리소스를 조달하거나 구성, 관리할 필요가 없으며 사용한 만큼만 비용을 지불

### EC2, RDS 외의 AWS의 다른 서비스
- Amazon S3	: 직접적인 S3 on Outposts 사용량에는 모두 일치하는 계정과 버킷 CloudWatch 지표가 있습니다.
- Amazon Elastic Block Store(Amazon EBS) : Amazon EBS on Outposts의 경우 AWS Outpost를 스냅샷 대상으로 선택하고 S3 on Outpost에 로컬로 저장할 수 있습니다.
- Amazon Relational Database Service(Amazon RDS) : Amazon RDS 로컬 백업을 사용하여 RDS 백업을 Outpost에 로컬로 저장할 수 있습니다.
