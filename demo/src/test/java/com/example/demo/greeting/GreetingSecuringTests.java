package com.example.demo.greeting;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.user.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class GreetingSecuringTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void accessUnsecuredResourceThenOk() throws Exception {
		
		mockMvc.perform(get("/home")).andExpect(status().isOk()).andExpect(content().string(containsString("Welcome")));
	}
	
	@Test
	public void accessSecuredResourceUnauthenticatedThenRedirectsToLogin() throws Exception {
		
		mockMvc.perform(get("/hello")).andDo(print()).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	public void loginWithValidUserThenAuthenticated() throws Exception {
		
		FormLoginRequestBuilder loginRequest = formLogin().user("kupu").password("#1234");
		mockMvc.perform(loginRequest).andExpect(authenticated().withUsername("kupu"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void loginWithInvalidUserThenUnauthenticated() throws Exception {
		
		FormLoginRequestBuilder incorectPassword = formLogin().user("kupu").password("1234");
		mockMvc.perform(incorectPassword).andExpect(unauthenticated());
	}
	
	@Test
	@WithMockUser
	public void accessSecuredResourceAuthenticatedThenOk() throws Exception {
		
		mockMvc.perform(get("/hello")).andExpect(status().isOk());
	}
	
}
