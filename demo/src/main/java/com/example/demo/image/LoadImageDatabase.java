package com.example.demo.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.image.storageService.StorageService;

@Component
public class LoadImageDatabase implements CommandLineRunner{

	@Autowired
	private ImageRepository repository;
	
	@Autowired
	private StorageService service;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		repository.deleteAll();
		repository.save(new Image("stakes winner","horse.gif"));
		service.init();
	}

}
