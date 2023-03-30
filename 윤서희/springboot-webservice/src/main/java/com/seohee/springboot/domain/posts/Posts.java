package com.seohee.springboot.domain.posts;

import com.seohee.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity // JPA 어노테이션 -> 주요 어노테이션
public class Posts extends BaseTimeEntity {
    @Id // PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK의 생성 규칙을 나타냄
    private Long id;

    @Column(length = 500, nullable = false) // 테이블의 칼럼
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder // 롬복 어노테이션, 해당 클래스의 빌더 패턴 클래스 생성
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
