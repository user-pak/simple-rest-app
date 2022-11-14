package com.example.demo.post;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.WithMockCustomUser;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AccessingPostRestTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockCustomUser
	public void getMessageWithMockUser() throws Exception {
		
		mockMvc.perform(get("/hello")).andDo(print()).andExpect(status().isOk())
			.andExpect(content().string(Matchers.containsString("안녕하세요 popo!")));
	}
	
	@Test
	@WithMockCustomUser
	 public void shouldCreatePost() throws Exception {
		
		mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated());

	}
	
	@Test
	@WithMockCustomUser
	public void shouldReturnPost() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(get(url.replace("apiPosts", "posts"))).andDo(print()).andExpect(status().isOk())
		.andExpectAll(model().attribute("post", Matchers.hasProperty("nickname", Matchers.equalTo("popo"))),
				model().attribute("post", Matchers.hasProperty("createdOn", LocalDateTimeMatchers.before(LocalDateTime.now()))));
					
	}
		
	@Test
	@WithMockCustomUser
	public void shouldReplacePost() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(patch(url).content("{\"title\":\"바뀐 타이틀\",\"content\":\"바뀐 컨텐트\"}"))
			.andExpect(status().isNoContent());
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
		.andExpectAll(jsonPath("title", is("바뀐 타이틀")),
				jsonPath("nickname",is("popo")),
				jsonPath("content", is("바뀐 컨텐트")));
	}

	@Test
	@WithMockCustomUser
	public void shouldAddComment() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(post(url + "/postComments").contentType(MediaType.APPLICATION_JSON).content("{\"review\":\"포포의리뷰\"}"))
			.andExpect(status().isCreated())
			.andExpectAll(jsonPath("$.createdBy").doesNotExist(),
					jsonPath("$.nickname", is("popo")), 
					jsonPath("$.audit.createdOn").exists(),
					jsonPath("$.postComments[0].nickname", is("popo")),
					jsonPath("$.postComments[0].audit.createdOn").exists(),
					jsonPath("$.postComments[0].review", is("포포의리뷰")));		
	}
	
	@Test
	@WithMockCustomUser
	public void shoudDeletePostAndPostComment() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(post(url + "/postComments").contentType(MediaType.APPLICATION_JSON).content("{\"review\":\"포포의리뷰\"}"))
			.andExpect(status().isCreated());
		mockMvc.perform(delete(url)).andExpect(status().isNoContent());
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockCustomUser
	public void shouldReplaceComment() throws Exception {
		
		MvcResult postResult = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = postResult.getResponse().getHeader("Location");
		MvcResult commentResult = mockMvc.perform(post(url + "/postComments").contentType(MediaType.APPLICATION_JSON).content("{\"review\":\"포포의리뷰\"}"))
			.andExpect(status().isCreated()).andReturn();
		Long postCommentId = JsonPath.parse(commentResult.getResponse().getContentAsString()).read("$.postComments[0].id", Long.class);
		String urlWithCommentId = url + "/postComments/" + postCommentId;
		mockMvc.perform(put(urlWithCommentId)
				.contentType(MediaType.APPLICATION_JSON).content("{\"review\":\"바뀐 포포의리뷰\"}"))
				.andExpect(status().isCreated())
				.andExpectAll(jsonPath("$.audit.updatedOn").exists(),
						jsonPath("$.review", is("바뀐 포포의리뷰")));

	}
	
	@Test
	@WithMockCustomUser
	public void shouldDeleteComment() throws Exception {
		
		MvcResult postResult = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = postResult.getResponse().getHeader("Location");
		MvcResult commentResult = mockMvc.perform(post(url + "/postComments").contentType(MediaType.APPLICATION_JSON).content("{\"review\":\"포포의리뷰\"}"))
				.andExpect(status().isCreated()).andReturn();
		Long postCommentId = JsonPath.parse(commentResult.getResponse().getContentAsString()).read("$.postComments[0].id", Long.class);
		String urlWithCommentId = url + "/postComments/" + postCommentId;
		mockMvc.perform(delete(urlWithCommentId)).andExpect(status().isNoContent());
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk());
		
	}
	
}
