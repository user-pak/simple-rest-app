package com.example.demo.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class AccessingUserRestTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void shouldFindByNickname() throws Exception {
		
		mockMvc.perform(get("/apiUsers/search/findByNickname?nickname={nickname}", "kupu")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nickname").value("kupu"));
	}
	
	@Test
	public void shouldReplaceDescription() throws Exception {
		
		Integer id = userRepository.findByNickname("kupu").getId();
		mockMvc.perform(patch("/apiUsers/" + id).content("{\"description\":\"트레저 헌터\"}").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
		mockMvc.perform(get("/apiUsers/" + id)).andDo(print())
			.andExpectAll(jsonPath("nickname").value("kupu"), jsonPath("$.description").value("트레저 헌터"));
	}
	
}
