package com.javadev.organizer.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.javadev.organizer.controllers.StudentController;
import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.handlers.GlobalExceptionHandler;
import com.javadev.organizer.repositories.CourseRepository;
import com.javadev.organizer.repositories.UserRepository;
import com.javadev.organizer.services.StudentService;
import com.javadev.organizer.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StudentControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private CourseRepository courseRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private StudentService studentService;
	
	@Mock
	private UserService userService;
	
	@InjectMocks
	private StudentController studentController;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(studentController).setControllerAdvice(new GlobalExceptionHandler()).build();
	}
	
	@Test
	public void shouldFindUserById() throws Exception {
		User expectedUser = new User.Builder().email("test@mail.com").firstName("Jan").lastName("Kowalski").role(Role.STUDENT).build();
		when(userService.getUserById(1L)).thenReturn(expectedUser);
		
		mockMvc.perform(get("/api/user/1")).andExpect(status().isOk());
	}
	
	@Test
	public void shouldReturnCoursesIds() throws Exception {
		Map<Long, Boolean> coursesStatus = expectedCoursesStatus();	
		when(studentService.getCoursesStatusByUserId(1L)).thenReturn(coursesStatus);
		
		mockMvc.perform(get("/api/student/{id}/courses/", 1L))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void shouldReturnCoursesIdsNotFound() throws Exception {
		when(studentService.getCoursesStatusByUserId(1L)).thenReturn(new HashMap<Long, Boolean>());
		
		mockMvc.perform(get("/api/student/checkCoursesStatus/{id}", 1L)).andExpect(status().isNotFound());
	}
	
	private Map<Long, Boolean> expectedCoursesStatus(){
		Map<Long, Boolean> coursesStatus = new HashMap<>();	
		coursesStatus.put(1L, true);
		coursesStatus.put(2L, true);
		coursesStatus.put(3L, false);
		
		return coursesStatus;
	}
}