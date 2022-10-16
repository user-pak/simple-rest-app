package com.example.demo.greeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class MySecurityConfiguration {
	
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		  		return (web) -> web.ignoring()
		  		// Spring Security should completely ignore URLs starting with /resources/
		  				.antMatchers("/resources/**");
	}	
	@Bean
	   	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	   		http
	   			.csrf().disable()
				.authorizeHttpRequests((requests) -> requests
					.antMatchers("/", "/home").permitAll()
//					.antMatchers("/**").permitAll()
					.antMatchers("/").hasRole("USER")
					.anyRequest().authenticated()
				)
				
				.formLogin((form) -> form
					.loginPage("/login")
					.permitAll()
					.successHandler(successHandler)
					.defaultSuccessUrl("/hello")
				)
				.logout((logout) -> logout.permitAll());
	   		return http.build();
	   	}
	  
//	 @Bean
//	   	public UserDetailsService userDetailsService() {
//	   		UserDetails user = User.withDefaultPasswordEncoder()
//	   			.username("user")
//	   			.password("password")
//	   			.roles("USER")
//	   			.build();
//	   		UserDetails admin = User.withDefaultPasswordEncoder()
//	   			.username("admin")
//	   			.password("password")
//	   			.roles("ADMIN", "USER")
//	   			.build();
//	   		return new InMemoryUserDetailsManager(user, admin);
//	   	}
	  
	   	// Possibly more bean methods ...
	@Bean
	public MyUserDetailsService customUserDetailsService() {
		return new MyUserDetailsService();
	}
}

