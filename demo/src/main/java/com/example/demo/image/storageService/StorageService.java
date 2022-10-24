package com.example.demo.image.storageService;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.image.Image;

public interface StorageService {
	
	void init();

	Resource loadAsResource(String filename);

	Path storeWithId(MultipartFile file, Image image);

	void deleteFile(Image image);

	String renameFile(Image image);



}
