package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadUserDatabase implements CommandLineRunner{
	
	private final UserRepository userRepository;
	
	@Autowired
	LoadUserDatabase(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		userRepository.deleteAll();
		userRepository.save(new User("kupu","chocobo","treasure hunter","#1234"));
	}

}
