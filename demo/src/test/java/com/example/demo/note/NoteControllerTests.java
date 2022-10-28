package com.example.demo.note;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private NoteRepository repository;
	
	@Test
	@WithMockUser
	public void shouldFindByIdThenReturnNote() throws Exception {
		
		when(repository.findById(1)).thenReturn(Optional.of(new Note("노트","컨텐트", "kupu")));
		mockMvc.perform(get("/notes/1")).andDo(print()).andExpect(model().attributeExists("note"));
	}

	@Test
	@WithMockUser(username="popo")
	public void shoudForbidReplaceNoteWhenInvalidWriter() throws Exception {
		
		when(repository.findById(1)).thenReturn(Optional.of(new Note("쿠푸타이틀","쿠푸컨텐트", "kupu")));
		mockMvc.perform(put("/notes/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"수정한쿠푸타이틀\",\"content\":\"수정한쿠푸내용\",\"writer\":\"kupu\"}"))
				.andExpect(status().isForbidden());
		
	}
}
