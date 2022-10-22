package com.example.demo.image.storageService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService{
	
	@Value("${storage.directory}")
	private String directory;
	
	@Override
	public Resource loadAsResource(String filename) {
		// TODO Auto-generated method stub
		try {
			Resource resource = new UrlResource(Paths.get(directory, filename).toUri());
			if(resource.exists()) return resource;
			else throw new StorageServiceException("could not find the file");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new StorageServiceException("could not read file");
		}
	}

	@Override
	public Path store(MultipartFile imageFile) {
		// TODO Auto-generated method stub
		if(imageFile.isEmpty()) throw new StorageServiceException("file is empty");	
		try (InputStream inputStream = imageFile.getInputStream()) {
			Files.copy(inputStream, Paths.get(directory, imageFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
			return Paths.get(directory, imageFile.getOriginalFilename());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new StorageServiceException("could not copy file");
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
			Files.createDirectories(Paths.get(directory));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new StorageServiceException("could not create directory");
		}
	}



}
