package com.example.demo.note;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.servlet.ModelAndView;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class NoteRepositoryTests {
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private NoteRepository repository;
	
	@BeforeEach
	public void deleteAllBeforeTests() throws Exception {
		repository.deleteAll();
	}
	
	@Test
	public void shouldReturnRepositoryIndex() throws Exception {
		
		mockMvc.perform(get("/apiNotes")).andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath("$._links").exists());
	}
	
	@Test
	public void shouldCreateEntity() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiNotes").content("{\"title\":\"??????\",\"content\":\"??????\"}"))
			.andExpect(status().isCreated()).andReturn();
		assertThat(result.getResponse().getHeader("Location").toString().contains("**/apiNotes"));
	}
	
	@Test
	public void shouldQueryFindByTitle() throws Exception {
		
		mockMvc.perform(post("/apiNotes").content("{\"title\":\"??????\",\"content\":\"??????\"}"))
			.andExpect(status().isCreated());
		mockMvc.perform(get("/apiNotes/search/findByTitle?title={title}", "??????")).andExpect(status().isOk())
			.andExpect(jsonPath("$.content").value("??????"));
	}

	@Test
	public void shouldReplaceEntity() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiNotes").content("{\"title\":\"??????\",\"content\":\"??????\"}"))
				.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(put(url).content("{\"title\":\"???????????????\",\"content\":\"???????????????\"}"))
			.andExpect(status().isNoContent());
		mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("???????????????")).andExpect(jsonPath("$.content").value("???????????????"));
	}
	
	@Test
	public void shouldDeleteEntity() throws Exception {
		
		MvcResult result =mockMvc.perform(post("/apiNotes").content("{\"title\":\"note\",\"content\":\"content\"}")).andExpect(status().isCreated()).andReturn();
		String uri = result.getResponse().getHeader("Location");
		mockMvc.perform(delete(uri)).andExpect(status().isNoContent());
		mockMvc.perform(get(uri)).andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser(username="kupu")
	public void shouldReplaceNoteWhenValidWriter() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/apiNotes").content("{\"title\":\"?????????\",\"content\":\"?????????\",\"writer\":\"kupu\"}").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated()).andReturn();
		String url = result.getResponse().getHeader("Location");
		mockMvc.perform(put(url.replace("apiNotes", "notes"))
				.contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"??????????????????\",\"content\":\"??????????????????\",\"writer\":\"kupu\"}"))
			.andExpect(status().is2xxSuccessful());
		mockMvc.perform(get(url.replace("apiNotes", "notes"))).andDo(print())
			.andExpectAll(model().attribute("note", Matchers.hasProperty("title", Matchers.equalTo("??????????????????"))),
						  model().attribute("note", Matchers.hasProperty("content", Matchers.equalTo("??????????????????"))),
						  model().attribute("note", Matchers.hasProperty("writer", Matchers.equalTo("kupu"))));
	}
}
