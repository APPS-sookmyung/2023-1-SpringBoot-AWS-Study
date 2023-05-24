# Chap06



- 외부인이 내가 만든 서비스에 접근하려면 24시간 작동하는 서버 필요
- 호스팅 서비스나 클라우드 서비스를 이용하여 가능
- 특정 시간에만 트래픽이 몰린다면 유동적으로 사양을 늘릴 수 있는 클라우드 서비스가 유리
- 클라우드
    - 인터넷(클라우드)을 통해 서버, 스토리지(파일 저장소), 데이터베이스, 네트워크, 소프트웨어, 모니터링 등의 컴퓨팅 서비스 제공
    - 개발자가 직접 해야 할 일을 클라우드가 전부 지원
- 클라우드 형태
    1. Infrastructure as a Service (IaaS, 아이아스, 이에스)
        - 기존 물리 장비를 미들웨어와 함께 묶어둔 추상화 서비스
        - 인프라 대여 서비스
        - AWS의 EC2, S3 등
    2. Platform as a Service (PaaS, 파스)
        - IaaS에서 한 번 더 추상화한 서비스
        - 더 많은 기능이 자동화 되어 있음
        - AWS의 Beanstalk, Heroku 등
    3. Software as a Service (SaaS, 사스)
        - 소프트웨어 서비스
        - 구글 드라이브, 드랍박스, 와탭 등
- AWS 선정 이유
    - 첫 가입 시 1년간 대부분 서비스 무료
    - 클라우드에서는 기본적으로 지원하는 기능이 많아 개인이나 소규모일 때 개발에 더 집중 가능
    - 국내 점유율 압도적
    - 사용자가 많아 국내 자료와 커뮤니티 활성화

### **6.1 AWS 회원 가입**

- [클라우드 서비스 | 클라우드 컴퓨팅 솔루션| Amazon Web Services](https://aws.amazon.com/ko/)  에서 회원 가입

[클라우드 서비스 | 클라우드 컴퓨팅 솔루션| Amazon Web Services

aws.amazon.com](https://aws.amazon.com/ko/)

### **6.2 EC2 인스턴스 생성**

- EC2
    - AWS에서 제공하는 성능, 용량 등을 유동적으로 사용할 수 있는 서버
    - AWS에서 리눅스 서버 혹은 윈도우 서버를 사용하는 것
- 리전
    - AWS의 서비스가 구동될 지역
    - AWS은 도시별로 클라우드 센터를 지어 해당 센터에 구축된 가상머신들을 사용 가능
1. 리전을 서울로 변경

![https://blog.kakaocdn.net/dn/uTxJi/btseyMOC0P8/Lx79yFG2ywGYLyeyHQ5Obk/img.png](https://blog.kakaocdn.net/dn/uTxJi/btseyMOC0P8/Lx79yFG2ywGYLyeyHQ5Obk/img.png)

2. EC2 서비스 선택

3. 인스턴스 시작 선택

- AMI(아마존 머신 이미지) 선택이 우선
- AMI란 EC2 인스턴스를 시작하는 데 필요한 정보를 이미지로 마들어 둔 것, 인스턴스라는 가상 머신에 운영체제 등을 설치할 수 있게 구워 넣은 이미지의 개념

4. Amazon Linux AMI 선택

![https://blog.kakaocdn.net/dn/bhYEUT/btseAUkPgh6/OkFbRM3pYeyXXUPLstS63K/img.png](https://blog.kakaocdn.net/dn/bhYEUT/btseAUkPgh6/OkFbRM3pYeyXXUPLstS63K/img.png)

5. 인스턴스 유형을 프리티어로 표기된 t2.microm 선택

![https://blog.kakaocdn.net/dn/nTvh4/btseyNUjLV8/TuUY4SZZ4dWZFcn864aY81/img.png](https://blog.kakaocdn.net/dn/nTvh4/btseyNUjLV8/TuUY4SZZ4dWZFcn864aY81/img.png)

- 크레딧이라는 일종의 CPU를 사용할 수 있는 포인트 개념
- 인스턴스 크기에 따라 정해진 비율로 CPU 크레딧을 계속 받게 됨
- 사용하지 않을 때는 크레딧을 축적하고 사용할 때 이 크레딧 사용

6. 스토리지 크기를 30GB로 설정

- 하드디스크라고 부르는 서버의 디스크
- 서버의 용량을 설정

7. 보안그룹 설정

- 보안그룹 = 방화벽
- 유형 항목이 SSH & 포트 항목이 22 => AWS EC2에 터미널로 접속할 때를 이야기

8. 키 페어 생성

- 절대 외부 유출 X

9. 인스턴스 시작

10. 고정 IP 설정

- 인스턴스의 IP가 매번 변경되지 않고 고정 IP를 가지도록 할당
- EIP 할당
    - AWS의 고정 IP = Elastic IP = EIP = 탄력적 IP

10. 탄력적 IP 할당

![https://blog.kakaocdn.net/dn/IwzCg/btseztawc5q/F2lb8qEtxyo3LRbuAGTnq1/img.png](https://blog.kakaocdn.net/dn/IwzCg/btseztawc5q/F2lb8qEtxyo3LRbuAGTnq1/img.png)

- 고정 IP 설정
    - 인스턴스의 IP가 매번 변경되지 않고 고정 IP를 가지도록 할당
    - AWS의 고정 IP = Elastic IP = EIP = 탄력적 IP

11. 탄력적 IP 주소 연결

- 탄력적 IP는 생성하고 EC2 서버에 연결하지 않으면 비용 발생

### **6.3 EC2 서버 접속**

- Windows의 경우

1. [www.putty.org](http://www.putty.org/) 에 접속 후 다운로드

- putty.exe & puttygen.exe 다운로드 후 puttygen.exe 실행
- puttygen : putty가 pem 키로 사용이 안되기 때문에 이를 ppk 파일로 변환해주는 클라이언트

![https://blog.kakaocdn.net/dn/NxyiS/btseBKJfoxX/05KyNgYOGXdRFfVmEjRYs0/img.png](https://blog.kakaocdn.net/dn/NxyiS/btseBKJfoxX/05KyNgYOGXdRFfVmEjRYs0/img.png)

2. 이전의 pem 키를 불러온 후 'Save private key' 선택

3. putty.exe 실행 후 설정

- HostName : username@탄력적 IP 주소
- Port : ssh 접속 포트인 22 등록
- Connection type : SSH 선택

4. 왼쪽 사이드바의 Connection => SSH => Auth => Credentials => Browse 이동하여 이전에 생성한 ppk 파일 불러오기

5. Session => Saved Sessions에 현재 설정들을 저장할 이름을 등록 후 Save

6. open 버튼 클릭으로 SHH 접속하기

### **6.4 아마존 리눅스 1 서버 생성 시 꼭 해야 할 설정들**

1. Java 8 설치
2. 타임존 변경
    - 기본 서버의 시간은 미국 시간대
    - 한국 시간대가 되어야만 우리가 사용하는 시간이 모두 한국 시간으로 등록되고 사용됨
3. 호스트 네임 변경
    - 현재 접속한 서버의 별명 등록
