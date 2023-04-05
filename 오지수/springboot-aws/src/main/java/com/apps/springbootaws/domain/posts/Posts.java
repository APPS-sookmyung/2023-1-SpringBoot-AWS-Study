package com.apps.springbootaws.domain.posts;

import com.apps.springbootaws.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor  // 기본 생성자 자동 추가
public class Posts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false) // 사이즈를 500으로 늘림.
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)    // 타입을 TEXT로 변경.
    private String content;
    private String author;

    @Builder    // 빌더 패턴 - 어느 필드에 어떤 값을 채워줘야 할지 명확하게 인지 가능.
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
