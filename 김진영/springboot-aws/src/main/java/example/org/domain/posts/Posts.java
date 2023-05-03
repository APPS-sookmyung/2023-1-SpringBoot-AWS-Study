package example.org.domain.posts;

import example.org.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter //Lombok 어노테이션 , 클래스 내 모든 필드의 Getter 메소드를 자동 생성
@NoArgsConstructor //Lombok 어노테이션 , 기본 생성자 자동 추가
@Entity //JPA 어노테이션 , 테이블과 링크될 클래스임을 나타냄
public class Posts extends BaseTimeEntity { //실제 DB의 테이블과 매칭될 클래스 (=Entity 클래스)
    @Id //PK필드
    @GeneratedValue(strategy = IDENTITY) //PK 생성 규칙
    private Long id;

    //테이블의 칼럼. 굳이 선언하지 않아도 해당 클래스의 필드는 모두 컬럼이 됨
    //기본 값 이외의 추가로 변경이 필요한 옵션이 있으면 사용함
    @Column(length = 500,nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder //해당 클래스의 빌더 패턴 클래스를 생성
    public Posts(String title, String content, String author){
        this.title=title;
        this.content=content;
        this.author=author;
    }

    public void update(String title,String content){
        this.title=title;
        this.content=content;
    }

    public void delete(){

    }
}
