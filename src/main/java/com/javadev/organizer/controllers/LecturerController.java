package com.javadev.organizer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javadev.organizer.entities.Role;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.repositories.UserRepository;

@RestController
public class LecturerController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/lecturer/find_all_students")
	public List<User> getAllStudents() {
		return userRepository.findByRole(Role.STUDENT.name());
	}
	
	@PostMapping("/lecturer/create_student")
	public void saveStudent(@RequestBody User student) {
		student.setRole(Role.STUDENT.name());
		
		userRepository.save(student);
	}
	
	@PatchMapping("/lecturer/update_student/{id}")
	public void updateStudent(
			@RequestBody User updatedStudent, 
			@PathVariable Long id) {
		//todo	
	}
	
	@DeleteMapping("/lecturer/remove_student/{id}")
	public void deleteStudent(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
}