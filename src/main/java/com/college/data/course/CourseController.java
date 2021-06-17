package com.college.data.course;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    private Logger logger;

    public CourseController () {
        logger = LoggerFactory.getLogger(CourseController.class);
    }

    // Mappings for GET calls to course service

    @RequestMapping ("/data/courses")
    public List<Course> showCourses () {
        return courseService.showCourses();
    }

    @RequestMapping ("/data/courses/{course_name_or_id}")
    public Course showCourse (
        @PathVariable ("course_name_or_id")  
        String courseNameOrId
    ) {
        
        Course course;
        
        try {
            // Assume 'courseNameOrId' to represent course id, intially
            course = courseService.showCourseById (courseNameOrId);    
        } catch (NoSuchElementException e1) {
            try {
                // Once course id assumption fails, then assume 'courseNameOrId' to be course name
                course = courseService.showCourseByName (courseNameOrId);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                course = null;
            }
        }
        
        return course;
    }


    // Mappings for POST calls to course service
    
    @RequestMapping (method = RequestMethod.POST, value = "/data/course")
    public ResponseEntity<String> addCourse (@Valid @RequestBody Course course) {

        courseService.addCourse (course);            
        return ResponseEntity.ok (course.toString ());
    
    }

    @RequestMapping (method = RequestMethod.POST, value = "/data/courses")
    public ResponseEntity<String> addCourses (@RequestBody List<@Valid Course> courses) {
        
        for (Course course : courses) {
                courseService.addCourse (course);            
        }    

        return ResponseEntity.ok (courses.toString ());
    
    }

    // Mappings for PUT calls to course service
    
    @RequestMapping (method = RequestMethod.PUT, value = "/data/course")
    public ResponseEntity<String> updateCourse (@Valid @RequestBody Course course) {
        
        courseService.updateCourse (course);            
        return ResponseEntity.ok (course.toString ());

    }

    @RequestMapping (method = RequestMethod.PUT, value = "/data/courses")
    public ResponseEntity<String> updateCourses (@RequestBody List<@Valid Course> courses) {

        for (Course course : courses) {
            courseService.updateCourse (course);
        }    
               
        return ResponseEntity.ok (courses.toString ());
    }

    // Mappings for DELETE calls to course service

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/courses/{course_name_or_id}")
    public void deleteCourse (
        @PathVariable ("course_name_or_id")
        String courseNameOrId 
    ) {
        
        try {
            // Assume 'courseNameOrId' to represent course id, intially
            courseService.deleteCourseById (courseNameOrId);    
        } catch (EmptyResultDataAccessException e1) {
            try {
                // Once course id assumption fails, then assume 'courseNameOrId' to be course name
                courseService.deleteCourseByName (courseNameOrId);
            } catch (EmptyResultDataAccessException e2) {
                // Once both the assumptions fail, then simply return
                System.out.println ("Failed to delete!");
                return;
            }
        }

    }

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/courses")
    public void deleteCourses (
        @RequestBody 
        List<@NotBlank @NotNull String> courseNameOrIds
    ) {

        for (String courseNameOrId : courseNameOrIds)
            deleteCourse (courseNameOrId);

    }

    // Exception handlers

    @ExceptionHandler(MismatchedInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMismatchedInputException (
        MismatchedInputException exception
    ) {
        logger.info ("Recieved data with incorrect syntax");
        return ResponseEntity
            .status (HttpStatus.BAD_REQUEST)
            .body (exception.getMessage());   
    }

    @ExceptionHandler (DataIntegrityViolationException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDataIntegrityViolationException (
        DataIntegrityViolationException exception
    ) {
        logger.info ("Recieved data in clear violation with data integrity");
        return ResponseEntity
            .status (HttpStatus.BAD_REQUEST)
            .body (exception.getMessage());
    }

    @ExceptionHandler (HttpMessageNotReadableException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException exception
    ) {
        logger.info ("Failed to read the contents of the request");
        return ResponseEntity
            .status (HttpStatus.BAD_REQUEST)
            .body (exception.getMessage());
    }

}
