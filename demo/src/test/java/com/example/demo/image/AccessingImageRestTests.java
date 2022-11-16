package com.example.demo.image;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.WithMockCustomUser;

@SpringBootTest
@AutoConfigureMockMvc
public class AccessingImageRestTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ImageDBRepository repository;
	
	@Test
	@WithMockCustomUser
	public void shouldUploadAndGetImage() throws Exception {
		
		MockMultipartFile mockFile = new MockMultipartFile("file", "목멀티파트파일.gif", "image/jpeg", "목멀티파트파일".getBytes());
		mockMvc.perform(multipart("/images").file(mockFile)
				.param("imageTitle", "파일")
				.contentType(MediaType.IMAGE_GIF))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", Matchers.containsString("images")));

		mockMvc.perform(get("/images")).andDo(print())
			.andExpect(status().isOk())
			.andExpectAll(model().attribute("images", 
					Matchers.hasItem(Matchers.allOf(
							Matchers.hasProperty("imageFilename", Matchers.containsString("목멀티파트파일.gif"))))),
					model().attribute("images", 
							Matchers.hasItem(Matchers.allOf(
									Matchers.hasProperty("nickname", Matchers.containsString("popo"))))),
					model().attribute("images", 
							Matchers.hasItem(Matchers.allOf(
									Matchers.hasProperty("audit")))));
	}
	
	@Test
	@WithMockCustomUser
	public void shouldDownloadImage() throws Exception {
		Long id = repository.save(new Image("이미지", "horse.gif", "horse".getBytes())).getId();
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<byte[]> response = restTemplate.getForEntity("http://localhost:8080/files/{id}", byte[].class, id);
		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(response.getHeaders()).toString().contains("attachment; filename=");
		assertThat(response.getHeaders().toString().endsWith("horse.gif"));
		assertThat(response.getBody()).isNotNull();	
	}
	
	@Test
	@WithMockCustomUser
	public void shouldReplaceImageTitle() throws Exception {
		MvcResult result = mockMvc.perform(post("/apiImages").content("{\"imageTitle\":\"이미지제목\"}"))
			.andExpect(status().isCreated()).andReturn();
		String uri = result.getResponse().getHeader("Location");
		mockMvc.perform(patch(uri).content("{\"imageTitle\":\"수정한이미지제목\"}"))
			.andExpect(status().isNoContent());
		mockMvc.perform(get(uri)).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.imageTitle").value("수정한이미지제목"));
		mockMvc.perform(get("/images")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("images", 
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("imageTitle", Matchers.containsString("수정한이미지제목"))))));
	}
	@Test
	@WithMockCustomUser
	public void shouldDeleteImage() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiImages").content("{\"imageTitle\":\"이미지타이틀\"}"))
				.andExpect(status().isCreated()).andReturn();
		String uri = result.getResponse().getHeader("Location");
		mockMvc.perform(delete(uri))
			.andExpect(status().isNoContent());
		mockMvc.perform(get(uri)).andExpect(status().isNotFound());
		mockMvc.perform(get("/images")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("images", Matchers.not(Matchers.hasItem(
					Matchers.allOf(Matchers.hasProperty("imageTitle", Matchers.containsString("이미지타이틀")))))));
	}
	
}
