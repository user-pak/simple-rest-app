package com.example.demo.greeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;


public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = repository.findByNickname(nickname);
		if(user == null) throw new UsernameNotFoundException(nickname);
		return org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
				.username(user.getNickname())
				.password(user.getPassword())
				.roles("USER")
				.build();
	}
	

}
