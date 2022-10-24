package com.example.demo.image.storageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.util.FileSystemUtils;

import com.example.demo.image.Image;
import com.example.demo.image.ImageRepository;

@SpringBootTest
public class FileSystemImageStorageServiceTests {

	@Autowired
	private StorageService service;
	
	@Value("${storage.directory}")
	private String directory;
	
	@Autowired
	private ImageRepository repository;
	
	@BeforeTestClass
	public void deleteAllFiles() throws Exception {			
		FileSystemUtils.deleteRecursively(Paths.get(directory).toFile());
		repository.deleteAll();
	}	
	
	@Test
	public void shouldCreateDirectory() {		
		service.init();
		assertThat(Files.exists(Paths.get(directory)));
	}
	
	@Test
	public void shouldStoreImageThenReturnPath() throws Exception {	
		MockMultipartFile file = new MockMultipartFile("foo", "foo.gif", MediaType.IMAGE_GIF_VALUE, "foo".getBytes());
		Image image = repository.save(new Image("foo", file.getOriginalFilename()));
		Path path = service.storeWithId(file, image);
		assertThat(path.toString()).startsWith(directory).endsWith("foo.gif");
		assertTrue(Files.exists(path));
	}
	
	@Test
	public void shouldLoadAsResource() throws Exception {
		MockMultipartFile file = new MockMultipartFile("goo", "goo.gif", MediaType.IMAGE_GIF_VALUE, "foo".getBytes());
		Image image = repository.save(new Image("goo", file.getOriginalFilename()));
		Path path = service.storeWithId(file, image);
		Resource resource = service.loadAsResource(path.getFileName().toString());
		assertThat(resource.getURI()).asString().contains(directory, "goo.gif");
	}
	
	@Test
	public void whenLoadNonExistentShouldThrowException() throws Exception {
		assertThrows(StorageServiceException.class, () -> {
			service.loadAsResource("fake image");
		});
	}
	
	@Test
	public void shouldDeleteFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("hoo", "hoo.gif", MediaType.IMAGE_GIF_VALUE, "foo".getBytes());
		Image image = repository.save(new Image("hoo", file.getOriginalFilename()));
		String rename = service.renameFile(image);
		service.storeWithId(file, image);
		service.deleteFile(image);
		assertThat(Files.notExists(Paths.get(directory, rename), LinkOption.NOFOLLOW_LINKS));
	}
}
