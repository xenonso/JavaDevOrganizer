package com.javadev.organizer.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.javadev.organizer.entities.Course;
import com.javadev.organizer.entities.User;
import com.javadev.organizer.exceptions.PresenceNotSavedException;
import com.javadev.organizer.repositories.CourseRepository;
import com.javadev.organizer.repositories.UserRepository;

@Service
public class StudentService {
	private CourseRepository courseRepository;
	private UserRepository userRepository;

	@Autowired
	public StudentService(UserRepository userRepository, CourseRepository courseRepository) {
		this.userRepository = userRepository;
		this.courseRepository = courseRepository;
	}

	public void registerUserPresence(
			@RequestParam(value = "courseId", required = true) Long courseId,
			@RequestParam(value = "userId", required = true) Long userId) {

		User user = userRepository.findById(userId).orElse(null);
		Course course = courseRepository.findById(courseId).orElse(null);

		if (course != null || user != null) {

			if (isPresenceNotRegistered(user, course) && isRegisteredAfterCourse(course)) {
				savePresence(user, course);
			} else {
				throw new PresenceNotSavedException();
			}
		} else {
			throw new PresenceNotSavedException();
		}
	}

	public HttpEntity<List<BigDecimal>> getCoursesIdsByUser(@PathVariable Long id) {

		List<BigDecimal> courses = courseRepository.findCoursesIdsByUserId(id);

		if (courses.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return ResponseEntity.ok(courses);
		}
	}

	private boolean isPresenceNotRegistered(User user, Course course) {
		if (course.getUsers().contains(user)) {
			return false;
		} else {
			return true;
		}
	}

	private void savePresence(User user, Course course) {
		course.getUsers().add(user);
		courseRepository.save(course);
	}

	private boolean isRegisteredAfterCourse(Course course) {
		Date today = new Date(System.currentTimeMillis());
		if (course.getDate().before(today)) {
			return true;
		} else {
			return false;
		}
	}
}