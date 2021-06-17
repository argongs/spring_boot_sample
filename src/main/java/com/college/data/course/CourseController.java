package com.college.data.course;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping ("/data/courses/{course_info}")
    public Course showCourse (
        @PathVariable ("course_info") 
        @NotBlank 
        @NotNull 
        String courseInfo
    ) {
        
        Course course;
        
        try {
            // Assume 'courseInfo' to represent course id, intially
            course = courseService.showCourseById (courseInfo);    
        } catch (NoSuchElementException e1) {
            try {
                // Once course id assumption fails, then assume 'courseInfo' to be course name
                course = courseService.showCourseByName (courseInfo);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                course = null;
            }
        }
        
        return course;
    }


    // Mappings for POST calls to course service
    
    @RequestMapping (method = RequestMethod.POST, value = "/data/course")
    public ResponseEntity<String> addCourse (@RequestBody Course course) {
        
        if (course.isInvalid ())
            return new ResponseEntity<String> (
                "Invalid input", 
                HttpStatus.BAD_REQUEST
            );
        else {
            courseService.addCourse (course);            
            return ResponseEntity.ok ("Valid input");
        }

    }

    @RequestMapping (method = RequestMethod.POST, value = "/data/courses")
    public ResponseEntity<String> addCourses (@RequestBody List<Course> courses) {
        
        for (Course course : courses) {
            if (course.isInvalid ())
                return new ResponseEntity<String> (
                    "Invalid input", 
                    HttpStatus.BAD_REQUEST
                );
            else {
                courseService.addCourse (course);            
            }
        }    

        return ResponseEntity.ok ("Valid input");
    }

    // Mappings for PUT calls to course service
    
    @RequestMapping (method = RequestMethod.PUT, value = "/data/course")
    public ResponseEntity<String> updateCourse (@RequestBody Course course) {
        
        if (course.isInvalid ())
            return new ResponseEntity<String> (
                "Invalid input", 
                HttpStatus.BAD_REQUEST
            );
        else {
            courseService.updateCourse (course);            
            return ResponseEntity.ok ("Valid input");
        }

    }

    @RequestMapping (method = RequestMethod.PUT, value = "/data/courses")
    public ResponseEntity<String> updateCourses (@RequestBody List<Course> courses) {

        for (Course course : courses) {
            if (course.isInvalid ())
                return new ResponseEntity<String> (
                    "Invalid input", 
                    HttpStatus.BAD_REQUEST
                );
            else {
                courseService.updateCourse (course);
            }
        }    
               
        return ResponseEntity.ok ("Valid input");
    }

    // Mappings for DELETE calls to course service

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/courses/{course_info}")
    public void deleteCourse (
        @PathVariable ("course_info") 
        @NotBlank 
        @NotNull 
        String courseInfo 
    ) {
        
        try {
            // Assume 'courseInfo' to represent course id, intially
            courseService.deleteCourseById (courseInfo);    
        } catch (EmptyResultDataAccessException e1) {
            try {
                // Once course id assumption fails, then assume 'courseInfo' to be course name
                courseService.deleteCourseByName (courseInfo);
            } catch (EmptyResultDataAccessException e2) {
                // Once both the assumptions fail, then simply return
                System.out.println ("Failed to delete!");
                return;
            }
        }

    }

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/courses")
    public void deleteCourses (@RequestBody List<String> courseInfos) {

        for (String courseInfo : courseInfos)
            deleteCourse (courseInfo);

    }

    // Exception handlers

    @ExceptionHandler(MismatchedInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMismatchedInputException(
        MismatchedInputException exception
    ) {
        logger.info("Recieved data with incorrect syntax");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());   
    }
}
