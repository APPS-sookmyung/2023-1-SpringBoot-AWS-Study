## Chapter 8 : EC2 서버에 프로젝트를 배포해 보자

### [1] EC2에 프로젝트 Clone 받기

1. EC2에 깃 설치

- `sudo yum install git`
- 프로젝트 저장할 디렉토리 생성 : `mkdir ~/app && mkdir ~/app/step1`
- 생성된 디렉토리로 이동
- 깃허브 페이지 https 주소 복사
- git clone 진행 : `git clone 복사한 주소`
- 코드 잘 수행되는지 확인 : `./gradlew test`

### [2] 배포 스크립트 만들기

- 배포 : 작성한 코드를 서버에 반영하는 것
- 배포할 때마다 개발자가 하나하나 명령어로 실행하는 것은 불편함이 많음
- 이를 쉘 스크립트로 작성하여 실행
- 쉘 스크립트 : .sh 파일 확장자 가짐

1. deploy.sh 파일 생성
   `vim ~/app/step1/deploy.sh`

- 코드 추가
- 실행 권한 추가 : `chmod +x ./deploy.sh`
- 스크립트 실행 : `./deploy.sh`
- nohup.out(실행되는 애플리케이션에서 출력되는 모든 내용 가짐) 파일 열어 로그 확인 : `vim nohup.out`
- 하단에 보면 ClientRegistrationRepository that could not be found 오류 발생

### [2] 외부 Security 파일 등록하기

- 오류 발생 이유
  - ClientRegistrationRepository 생성하기 위해서는 clientId와 clientSecret가 필수
  - 로컬에서 실행 시 application-oauth.properties가 있어 묹 없었음
  - .gitignore에서 git 제외 대상이기 때문에 깃허브에는 올라가지 않음
  - 따라서 서버에서 직접 이 설정들을 가지고 있게 해야 함

1. app/propertires 파일 생성
   `vim /home/ec2-user/app/application-oauth.properties`
2. 로컬에 있는 application-oauth.properties 내용 copy 후 저장
3. 방금 생성한 application-oauth.properties를 쓰도록 deploy.sh 파일 수정
4. deploy.sh 파일 실행
