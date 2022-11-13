package com.example.demo.audit;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

public class SpringSecurityAuditorAware implements AuditorAware<User>{

	@Autowired
	private UserRepository repository;
	
	@Override
	public Optional<User> getCurrentAuditor() {
		// TODO Auto-generated method stub
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	      return null;
	    }
	    
		return Optional.of(repository.findByNickname(authentication.getName()));
		
	}


}
