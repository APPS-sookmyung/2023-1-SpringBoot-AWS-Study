스프링 부트와 AWS로 혼자 구현하는 웹 서비스

# Week 5

## 아마존 리눅스 서버 설정 - 시스템 호스트 이름 변경하기

전 domain name 설정에서 어려움을 겪었기에.. 그 부분을 정리합니다

[참고사이트 - Amazon Linux 인스턴스에서 호스트 이름 변경](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/set-hostname.html)

<br>

### 1. 호스트 이름 업데이트 유지 설정

호스트 이름 업데이트를 유지하려면 preserve_hostname cloud-init 설정이 true로 설정되어 있는지 확인해야 함.

```shell
sudo vi /etc/cloud/cloud.cfg
```

```shell
preserve_hostname: true
```

<br>

### 2. hostnamectl 명령으로 호스트 이름을 설정하여 원하는 시스템 호스트 이름을 반영하기

```shell
sudo hostnamectl set-hostname lucy-springboot-aws.localdomain
```

<br>

### 3. `/etc/sysconfig/network` 구성 파일을 열어 `HOSTNAME` 항목 변경하기

```
sudo vim /etc/sysconfig/network
```

```
HOSTNAME=lucy-springboot-aws.localdomain
```

<br>

### 4. `/etc/hosts` 파일을 열어 `127.0.0.1`로 시작되는 항목을 변경.

```
sudo vim /etc/hosts
```

```
127.0.0.1 lucy-springboot-aws.localdomain lucy-springboot-aws localhost4 localhost4.localdomain4
```

<br>

### 5. 인스턴스 재부팅

<br>

## 과제

### 1. 클라우드 컴퓨팅이란?

[참고사이트](https://aws.amazon.com/ko/what-is-cloud-computing/)

클라우드 컴퓨팅은 IT 리소스를 인터넷을 통해 온디맨드로 제공하고 사용한 만큼만 비용을 지불하는 것을 말합니다.

#### 클라우드 컴퓨팅의 이점

1. 민첩성 <br>
   클라우드를 통해 광범위한 기술에 쉽게 액세스할 수 있으므로, 더 빠르게 혁신하고 상상할 수 있는 거의 모든 것을 구축할 수 있습니다.
2. 탄력성 <br>
   클라우드 컴퓨팅을 사용하면 향후 최고 수준의 비즈니스 활동을 처리하기 위해 리소스를 사전에 오버 프로비저닝할 필요가 없습니다.
3. 비용 절감 <br>
   클라우드를 통해 고정 비용(데이터 센터, 물리적 서버 등)을 가변 비용으로 전환하고, 사용한 만큼만 IT 비용을 지불할 수 있습니다.
4. 몇 분 만에 전 세계에 배포 <br>
   클라우드를 사용하면 몇 분 만에 새로운 지리적 리전으로 확장하고 전 세계에 배포할 수 있습니다.

<br>

### 2. AWS의 다른 서비스들 조사

#### - S3 (정적 파일 관리)

정적 파일 (사진, 비디오, 문서 등 또는 frontend 코드와 Lambda 함수 코드도 해당) 스토리지 서비스의 솔루션

- 오브젝트/정적 파일 스토리지 서비스 (사진, 비디오, 문서 등 또는 코드 파일까지! )
- 간편한 데이터 관리 및 액세스 제어
- 비용 효율적인 다양한 스토리지 클래스 & 백업 및 복원 솔루션 제공

#### - Route 53 (DNS 설정)

AWS가 제공하는 DNS 설정 서비스

- Domain Name System (DNS) 도메인 관리/설정 서비스
- EC2 인스턴스, Elastic 로드 밸런서, S3 저장소 등 AWS 서비스 인프라에 효과적으로 연결

#### - Elastic Beanstalk

배포를 돕는 PaaS 같은 서비스

- 애플리케이션 배포를 간편히 관리해주는 서비스
- 코드를 업로드하기만 하면 Elastic Beanstalk가 용량 프로비저닝, 로드 밸런싱, Auto Scaling부터 시작하여 애플리케이션 상태 모니터링에 이르기까지 자동으로 처리

> PaaS란? <br>
> “플랫폼-같은-서비스(Platform-as-a-service, PaaS)”는 하드웨어 및 응용 프로그램 관리가 제3사를 통해 제공되는 클라우드 컴퓨팅의 한 형식
