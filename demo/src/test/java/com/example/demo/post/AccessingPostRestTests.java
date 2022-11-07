package com.example.demo.post;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class AccessingPostRestTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PostRepository postRepository;
	
	@BeforeEach
	public void deleteAllBeforeTests() throws Exception{
		
		postRepository.deleteAll();
	}
	
	@Test
	 public void shouldCreatePost() throws Exception {
		
		mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"name\":\"이름\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated());

	}
	
	@Test
	public void shouldReturnPost() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"name\":\"이름\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
			.andExpectAll(jsonPath("title", is("포스트")),jsonPath("name",is("이름")),jsonPath("content", is("컨텐트")));
	}
		
	@Test
	public void shouldReplacePost() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"name\":\"이름\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(patch(url).content("{\"title\":\"바뀐 타이틀\",\"content\":\"바뀐 컨텐트\"}"))
			.andExpect(status().isNoContent());
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
		.andExpectAll(jsonPath("title", is("바뀐 타이틀")),jsonPath("name",is("이름")),jsonPath("content", is("바뀐 컨텐트")));
	}

	@Test
	public void shouldAddComment() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"name\":\"이름\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(post(url + "/postComments").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"kupu\",\"review\":\"쿠푸의리뷰\"}"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.postComments[0].name", is("kupu")))
			.andExpect(jsonPath("$.postComments[0].review", is("쿠푸의리뷰")));		
	}
	
	@Test
	public void shoudDeletePostAndPostComment() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"name\":\"이름\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(post(url + "/postComments").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"kupu\",\"review\":\"쿠푸의리뷰\"}"))
			.andExpect(status().isCreated());
		mockMvc.perform(delete(url)).andExpect(status().isNoContent());
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldReplaceComment() throws Exception {
		
		MvcResult postResult = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"name\":\"이름\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = postResult.getResponse().getHeader("Location");
		MvcResult commentResult = mockMvc.perform(post(url + "/postComments").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"kupu\",\"review\":\"쿠푸의리뷰\"}"))
			.andExpect(status().isCreated()).andReturn();
		Long postCommentId = JsonPath.parse(commentResult.getResponse().getContentAsString()).read("$.postComments[0].id", Long.class);
		String urlWithCommentId = url + "/postComments/" + postCommentId;
		mockMvc.perform(put(urlWithCommentId)
				.contentType(MediaType.APPLICATION_JSON).content("{\"review\":\"바뀐 쿠푸의리뷰\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.review", is("바뀐 쿠푸의리뷰")));

	}
	
	@Test
	public void shouldDeleteComment() throws Exception {
		
		MvcResult postResult = mockMvc.perform(post("/apiPosts").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"포스트\",\"name\":\"이름\",\"content\":\"컨텐트\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = postResult.getResponse().getHeader("Location");
		MvcResult commentResult = mockMvc.perform(post(url + "/postComments").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"popo\",\"review\":\"포포의리뷰\"}"))
				.andExpect(status().isCreated()).andReturn();
		Long postCommentId = JsonPath.parse(commentResult.getResponse().getContentAsString()).read("$.postComments[0].id", Long.class);
		String urlWithCommentId = url + "/postComments/" + postCommentId;
		mockMvc.perform(delete(urlWithCommentId)).andExpect(status().isNoContent());
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk());
		
	}
	
}
