package com.jojoldu.book.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// 제거했던 JPA Auditing 다시 활성화
@EnableJpaAuditing
public class JpaConfig {}