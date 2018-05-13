package com.javadev.organizer.test;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadev.organizer.controllers.AdminController;
import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.UsersListNotFoundException;
import com.javadev.organizer.exceptions.handlers.GlobalUserExceptionsHandler;
import com.javadev.organizer.repositories.UserRepository;
import com.javadev.organizer.services.AdminService;

public class AdminControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AdminService adminService;
	
	@InjectMocks
	private AdminController adminController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(adminController).setControllerAdvice(new GlobalUserExceptionsHandler()).build();
	}

	@Test
	public void shouldReturnUserList() throws Exception {
		List<User> expectedUsers = getExpectedUsers();

		when(adminService.getAllUsers()).thenReturn(expectedUsers);

		mockMvc.perform(get("/admin/users")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].email", is("jkowalski@mail.com")))
				.andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].email", is("anowak@mail.com")));
	}

	@Test
	public void shouldReturnUserListNotFound() throws Exception {
		when(adminService.getAllUsers().isEmpty()).thenThrow(UsersListNotFoundException.class);
		mockMvc.perform(get("/admin/users")).andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldSaveUserWithUniqueEmail() throws Exception {
		User unsavedUser = new User.Builder().email("test@mail.com").firstName("Jan").lastName("Kowalski").role(Role.LECTURER).build();
		User savedUser = new User.Builder().id(1L).email("test@mail.com").firstName("Jan").lastName("Kowalski").role(Role.LECTURER).build();
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(unsavedUser);
		
		when(userRepository.countByEmail("test@mail.com")).thenReturn(0L);
		when(userRepository.save(unsavedUser)).thenReturn(savedUser);	
		
		mockMvc.perform(post("/admin/user").contentType( MediaType.APPLICATION_JSON).content(json))
			   .andExpect(status().isOk());
	}

	private List<User> getExpectedUsers() {
		List<User> expectedUsers = new ArrayList<>();
		expectedUsers.add(new User.Builder().id(1L).email("jkowalski@mail.com").build());
		expectedUsers.add(new User.Builder().id(2L).email("anowak@mail.com").build());

		return expectedUsers;
	}
}
