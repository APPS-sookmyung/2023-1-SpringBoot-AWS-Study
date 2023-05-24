package com.hailey.book.springboot.domain.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email); // 이미 생성됨 사용자인지 확인용 메서드
}
