package com.example.demo;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.example.demo.greeting.MyUserDetailsService;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser>{

	private final MyUserDetailsService myUserDetailsService;
	
	public WithMockCustomUserSecurityContextFactory(MyUserDetailsService myUserDetailsService) {
		
		this.myUserDetailsService = myUserDetailsService;
	}
	
	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
		// TODO Auto-generated method stub
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		UserDetails principal = myUserDetailsService.loadUserByUsername(withMockCustomUser.username());
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
		securityContext.setAuthentication(authentication);
		return securityContext;
	}

}
