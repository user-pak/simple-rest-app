package com.example.demo.note;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
		
		when(repository.findById(1)).thenReturn(Optional.of(new Note("note","content")));
		mockMvc.perform(get("/notes/1")).andDo(print()).andExpect(model().attributeExists("note"));
	}

}
