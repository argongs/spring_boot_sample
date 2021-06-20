package com.college.data.course;

import java.util.List;
import java.util.NoSuchElementException;

import com.college.data.student.Student;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping ("/data/courses")
    public List<Course> showCourses () {
        return courseService.showCourses ();
    }


    @RequestMapping ("/data/courses/{course_id_or_name}/students")
    public List<Student> showStudentsByCourseIdOrName (
        @PathVariable ("course_id_or_name") 
        String courseIdOrName
    ) {
        List<Student> students;
        
        try {
            // Assume 'courseIdOrName' to represent course id, intially
            students = courseService.showStudentsByCourseId (courseIdOrName);    
        } catch (NoSuchElementException e1) {
            try {
                // Once course id assumption fails, then assume 'courseIdOrName' to be course name
                students = courseService.showStudentsByCourseName (courseIdOrName);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                students = null;
            }
        }
        
        return students;
    }

    /* 
    * POST, PUT & DELETE calls won't be needed for courses, since any changes related to  
    * the course entities will be done through the Professor controller directly 
    */ 

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
