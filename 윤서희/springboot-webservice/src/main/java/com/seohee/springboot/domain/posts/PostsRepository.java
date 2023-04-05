package com.seohee.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    // <Entity 클래스, PK 타입>
    // CRUD 메소드 자동 생성
    // Entity 클래스와 기본 Entity Repository 는 함께 위치

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
