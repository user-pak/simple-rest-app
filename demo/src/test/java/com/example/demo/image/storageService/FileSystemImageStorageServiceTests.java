package com.example.demo.image.storageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.util.FileSystemUtils;

@SpringBootTest
public class FileSystemImageStorageServiceTests {

	@Autowired
	private StorageService service;
	
	@Value("${storage.directory}")
	private String directory;
	
	@BeforeTestClass
	public void deleteAllFiles() throws Exception {			
		FileSystemUtils.deleteRecursively(Paths.get(directory).toFile());		
	}	
	
	@Test
	public void shouldCreateDirectory() {		
		service.init();
		assertThat(Files.exists(Paths.get(directory)));
	}
	
	@Test
	public void shouldStoreImageThenReturnPath() throws Exception {		
		Path path = service.store(new MockMultipartFile("foo", "foo.gif", MediaType.IMAGE_GIF_VALUE, "foo".getBytes()));
		assertThat(path.toString()).startsWith(directory).endsWith("foo.gif");
		assertTrue(Files.exists(path));
	}
	
	@Test
	public void shouldLoadAsResource() throws Exception {
		Path path = service.store(new MockMultipartFile("goo", "goo.gif", MediaType.IMAGE_GIF_VALUE, "foo".getBytes()));
		Resource resource = service.loadAsResource(path.getFileName().toString());
		assertThat(resource.getURI()).asString().contains(directory, "goo.gif");
	}
	
	@Test
	public void whenLoadNonExistentShouldThrowException() throws Exception {
		assertThrows(StorageServiceException.class, () -> {
			service.loadAsResource("fake image");
		});
	}
}
