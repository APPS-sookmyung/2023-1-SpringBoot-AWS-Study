package example.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication //스프링부트의 자동 설정, 스프링 Bean읽기와 생성을 모두 자동으로 설정.
// 요게 있는 위치부터 설정을 읽어감.(항상 프로젝트 최상단에 위치해야!!)
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args); //내장 WAS(Web Application Server;별도로 외부에 WAS를 두지 않고 애플리케이션을 실행할 떄 내부에서 WAS를 실행하는 것) 실행
        //항상 서버에 TomCat을 설치할 필요x, 스프링 부트로 만들어진 Jar파일(실행 가능한 java 패키징 파일) 로 실행하면 됨.
        //스프링 부트에서는 내장 WAS를 쓰는 것을 권장. (언제 어디서나 같은 환경에서 스프링 부트를 배포할 수 있기 때문)
    }
}
