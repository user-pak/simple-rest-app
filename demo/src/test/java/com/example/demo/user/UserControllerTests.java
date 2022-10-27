package com.example.demo.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class UserControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldFindByNickname() throws Exception {
		
		mockMvc.perform(get("/user/kupu"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(model().attribute("profile", Matchers.hasProperty("nickname", Matchers.equalTo("kupu"))));
	}

}
