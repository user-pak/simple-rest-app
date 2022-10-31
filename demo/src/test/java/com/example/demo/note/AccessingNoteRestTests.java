package com.example.demo.note;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class AccessingNoteRestTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldQueryFindByTitle() throws Exception {
		
		mockMvc.perform(get("/apiNotes/search/findByTitle?title={title}", "노트1")).andExpect(status().isOk())
			.andExpectAll(jsonPath("$.content").value("쿠푸의노트"),
						  jsonPath("$.writer").value("kupu"),
						  jsonPath("$.comment")
						  .value(Matchers.hasToString(Matchers.containsString("{\"writer\":\"popo\",\"comment\":\"포포의코멘트\"}"))));
	
	}
}
