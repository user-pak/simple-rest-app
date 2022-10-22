package com.example.demo.image.storageService;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	
	void init();

	Resource loadAsResource(String filename);

	Path store(MultipartFile imageFile);

}
