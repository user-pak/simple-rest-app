package com.example.demo.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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

import com.example.demo.image.ImageRepository;
import com.example.demo.image.storageService.StorageService;
import com.example.demo.image.storageService.StorageServiceException;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class AccessingImageRestTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StorageService service;
	
	@Autowired
	private ImageRepository repository;
	
	@BeforeEach
	public void deleteAllBeforeTest() throws Exception {
		
		repository.deleteAll();
	}
	
	@Test
	public void shouldFindAllImageList() throws Exception {
		
		mockMvc.perform(get("/images")).andDo(print())
			.andExpect(status().isOk())
			.andExpectAll(model().attribute("images",
					Matchers.hasItem(Matchers.anyOf(Matchers.hasProperty("imageFilename", 
							Matchers.startsWith("http://localhost/files/"))))),
					model().attribute("images",
					Matchers.hasItem(Matchers.anyOf(Matchers.hasProperty("imageFilename",
							Matchers.endsWith("horse.gif"))))));
	}
	
	@Test
	public void shouldDownloadImage() throws Exception {
		ClassPathResource resource = new ClassPathResource("horse.gif", getClass());
		given(service.loadAsResource("horse.gif")).willReturn(resource);
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<Resource> response = restTemplate.getForEntity("http://localhost:8080/files/{filename}", Resource.class, "horse.gif");
		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(response.getHeaders()).toString().contains("attachment; filename=");
		assertThat(response.getHeaders().toString().endsWith("horse.gif"));
		assertThat(response.getBody()).isNotNull();
	
	}
	
	@Test
	public void uploadShouldSaveImageAndStoreFile() throws Exception {
		
		MockMultipartFile mockFile = new MockMultipartFile("file", "bar.gif", "image/jpeg", "bar data".getBytes());
		mockMvc.perform(multipart("/images").file(mockFile)
				.param("imageTitle", "bar")
				.contentType(MediaType.IMAGE_GIF))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", Matchers.containsString("images")));

		mockMvc.perform(get("/images")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("images", 
					Matchers.hasItem(Matchers.allOf(
							Matchers.hasProperty("imageFilename", Matchers.containsString("bar.gif"))))));
	}
	
	@Test
	public void should404WhenMissingFile() throws Exception {
		given(service.loadAsResource("fake_image")).willThrow(StorageServiceException.class);
		MvcResult result = mockMvc.perform(get("/files/fake_image")).andExpect(status().isNotFound()).andReturn();
		assertThat(result.getResponse().getContentAsString().contains("could not find the file"));
	}
	
	@Test
	public void shouldReplaceImageTitle() throws Exception {
		MvcResult result = mockMvc.perform(post("/apiImages").content("{\"imageTitle\":\"stakes winner\",\"imageFilename\":\"horse.gif\"}"))
			.andExpect(status().isCreated()).andReturn();
		String uri = result.getResponse().getHeader("Location");
		mockMvc.perform(patch(uri).content("{\"imageTitle\":\"stakes loser\"}"))
			.andExpect(status().isNoContent());
		mockMvc.perform(get(uri)).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.imageTitle").value("stakes loser"));
		mockMvc.perform(get("/images")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("images", 
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("imageTitle", Matchers.containsString("stakes loser"))))));
	}
	@Test
	public void shouldDeleteImage() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiImages").content("{\"imageTitle\":\"stakes winner\",\"imageFilename\":\"horse.gif\"}"))
				.andExpect(status().isCreated()).andReturn();
		String uri = result.getResponse().getHeader("Location");
		mockMvc.perform(delete(uri))
			.andExpect(status().isNoContent());
		mockMvc.perform(get(uri)).andExpect(status().isNotFound());
		mockMvc.perform(get("/images")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("images", Matchers.not(Matchers.hasItem(
					Matchers.allOf(Matchers.hasProperty("imageTitle", Matchers.containsString("stakes winner")))))));
	}
	
}
