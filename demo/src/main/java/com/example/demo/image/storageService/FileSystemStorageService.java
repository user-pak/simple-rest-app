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

import com.example.demo.image.Image;

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
	public void init() {
		// TODO Auto-generated method stub
		try {
			Files.createDirectories(Paths.get(directory));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new StorageServiceException("could not create directory");
		}
	}

	@Override
	public Path storeWithId(MultipartFile file, Image image) {
		// TODO Auto-generated method stub
		if(file.isEmpty()) throw new StorageServiceException("file is empty");
		Path temp = Paths.get(directory, file.getOriginalFilename());
		try (InputStream inputStream = file.getInputStream()){
			Files.copy(inputStream, temp, StandardCopyOption.REPLACE_EXISTING);
			String rename = renameFile(image);
			Files.move(temp, temp.resolveSibling(rename));
			return temp.resolveSibling(rename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new StorageServiceException("could not save file");
		}
	}

	@Override
	public void deleteFile(Image image) {
		// TODO Auto-generated method stub
		String rename = renameFile(image);
		try {
			Files.delete(Paths.get(directory, rename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new StorageServiceException("could not delete the file");
		}
	}

	@Override
	public String renameFile(Image image) {
		// TODO Auto-generated method stub
		return image.getImageId() + "_" + image.getImageFilename();
	}

}
