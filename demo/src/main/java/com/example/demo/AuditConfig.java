package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.example.demo.audit.SpringSecurityAuditorAware;
import com.example.demo.user.User;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

	  @Bean
	  public AuditorAware<User> auditorProvider() {
	    return new SpringSecurityAuditorAware();
	  }
}
