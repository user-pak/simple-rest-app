package com.example.demo.image;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.example.demo.image.storageService.StorageService;
import com.example.demo.image.storageService.StorageServiceException;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class ImageControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StorageService service;
	
	@Test
	public void shouldFindAllImageList() throws Exception {
		
		mockMvc.perform(get("/images")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("images",
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("imageFilename", 
							Matchers.containsString("http://localhost/files/horse.gif"))))));
	}
	
	@Test
	public void shouldDownloadImage() throws Exception {
		ClassPathResource resource = new ClassPathResource("horse.gif", getClass());
		given(service.loadAsResource("horse.gif")).willReturn(resource);
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<Resource> response = restTemplate.getForEntity("http://localhost:8080/files/{filename}", Resource.class, "horse.gif");
		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(response.getHeaders()).toString().contains("attachment; filename=horse.gif");
		assertThat(response.getBody()).isNotNull();
	
	}
	
	@Test
	public void uploadShouldSaveImageAndStoreFileThenReturnResponseEntity() throws Exception {
		
		MockMultipartFile mockFile = new MockMultipartFile("file", "bar.gif", "image/jpeg", "bar data".getBytes());
		mockMvc.perform(multipart("/images").file(mockFile)
				.param("imageTitle", "bar")
				.contentType(MediaType.IMAGE_GIF))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", Matchers.containsString("images")));

		then(service).should().store(mockFile);
		mockMvc.perform(get("/images")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("images", 
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("imageTitle", Matchers.containsString("bar"))))));
	}
	
	@Test
	public void should404WhenMissingFile() throws Exception {
		given(service.loadAsResource("fake_image")).willThrow(StorageServiceException.class);
		MvcResult result = mockMvc.perform(get("/files/fake_image")).andExpect(status().isNotFound()).andReturn();
		assertThat(result.getResponse().getContentAsString().contains("could not find the file"));
	}
}
