# Week7

## Chap08. EC2 서버에 프로젝트를 배포해보자

### 8.1 EC2에 프로젝트 Clone 받기

- git 설치 : `sudo yum install git`
- git clone해서 프로젝트 저장할 디렉토리 생성 : `mkdir ~/app && mkdir ~/app/step1`
- git clone한 후 clone 된 프로젝트로 이동 : tab 누르면 프로젝트명이 나옴

![image](https://github.com/seohee0925/2023-1-SpringBoot-AWS-Study/assets/102652293/f4d9415a-6557-470d-991f-563bdb73bbdf)


### 8.2 배포 스크립트 만들기

- 배포
    
    작성한 코드를 실제 서버에 반영하는 것
    
    - git clone / git pull =을 통해 새 버전의 프로젝트를 받음
    - Gradle이나 Maven을 통해 프로젝트 테스트와 빌드
    - EC2 서버에서 해당 프로젝트 실행 및 재실행
- 배포할 때마다 개발자가 하나하나 명령어를 실행하는 대신 쉘 스크립트로 작성해 스크립트만 실행
    
    ![image](https://github.com/seohee0925/2023-1-SpringBoot-AWS-Study/assets/102652293/d1f8bb7a-9839-4fc1-abc1-39caf217270f)
    
    - cp ./build/libs/*.jar $REPOSITORY/ : build의 결과물인 jar 파일을 복사해 jar 파일을 모아둔 위치로 복사
    - CURRENT_PID=$(pgrep -f springboot-webservice) : 기존에 수행 중이던 스프링 부트 애플리케이션을 종료
        
        ⇒ pgrep -f : 프로세스 이름으로 process id만 추출
        
    - JAR_NAME=$(ls -tr $REPOSITORY / | grep jar | tail -n 1) : 새로 실행할 jar 파일명을 찾고, 가장 나중의 jar 파일을 변수에 저장
    - nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 & : 찾은 jar 파일을 nohup으로 실행
        
        ⇒ 내장 톰캣을 사용하여 jar 파일만 있으면 바로 웹 애플리케이션 서버 실행 가능  
        
- 스크립트에 실행 권한 추가
    
    ![image](https://github.com/seohee0925/2023-1-SpringBoot-AWS-Study/assets/102652293/7e065177-d7ad-4c94-bcfc-a06ecd85bcaa)
    

### 8.3 외부 Security 파일 등록하기

- ClientRegistraionRepository를 생성 ⇒ clientId와 clientSecret이 필수
    
    ⇒ 공개된 저장소에 올릴 수 없으므로 서버에서 이 설정들을 가지고 있게 함
    

---

- app 디렉토리에 properties 파일 생성

### 8.4 스프링 부트 프로젝트로 RDS 접근하기

- RDS 테이블 생성
- 프로젝트 설정
    - build.gradle에 Maria DB 등록
        
        `implementation(”org.mariadb.jdbc:maraidb-java-client”)`
        
    - src/main/resources/ ⇒ [application-real.properties](http://application-real.properties) 파일 추가
- EC2 설정
    - application-real-db.properties
        
        ![image](https://github.com/seohee0925/2023-1-SpringBoot-AWS-Study/assets/102652293/1289ea85-7a38-4094-a6cd-8cfcd0c58d47)
        

### 8.5 EC2에서 소셜 로그인하기

- AWS 보안 그룹 변경 : 8080 포트가 보안 그룹에 열려 있는지 확인
- AWS EC2 도메인으로 접속 : EC2에 자동으로 할당된 도메인 이용
- 구글에 EC2 주소 등록 : http:// 없이 퍼블릭 DNS 등록
- 네이버에 EC2 주소 등록
    - 서비스 URL : 로그인ㅇ르 시도하는 서비스가 네이버에 등록된 서비스인지 판단
    - EC2 주소를 등록하면 localhost가 안되므로 개발 단계에서는 등록하지 않는 것을 추천
    - Callback URL : 전체 주소를 등록 (EC2 퍼블릭)

---

## vim 에디터 사용 정리

- Vim : 명령어 라인 사용자 인터페이스(CUI) 기반의 vi 호환 텍스트 편집기

> 모드 변환
> 
- i → 현재 커서의 위치에서부터 입력모드로 전환
- a → 현재 커서의 뒤에서부터 입력모드로 전환
- l → 현재 행의 맨 앞으로 이동해 입력모드로 전환
- ESC → 일반 모드로 전환

> 저장 및 종료
> 
- :w → 현재 파일 저장
- :q → 종료
- :wq → 저장 후 종료
- :q! → 변경된 내용을 무시하고 vi를 종료

> 삭제, 복사, 붙이기 명령
> 
- x → 현재 커서에 위치한 문자 하나 삭제
- #dd → 현재 커서의 위치부터 #행만큼 삭제
- #yy → 현재 커서의 위치부터 #행만큼 복사
