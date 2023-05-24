package example.org.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //Jpa Auditing 활성화
public class JpaConfig {
}
