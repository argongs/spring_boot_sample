package com.college.data.course;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.college.data.professor.Professor;
import com.college.data.professor.ProfessorService;
import com.college.data.student.Student;
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
    @Autowired
    private ProfessorService professorService;
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

    // Mappings for POST calls to course service
    
    // POST calls won't be needed for courses, since the additions to the course will be done 
    // only through the Professor entity

    // @RequestMapping (method = RequestMethod.POST, value = "/data/course")
    // public ResponseEntity<String> addCourse (@Valid @RequestBody Course course) {

    //     Professor professor = course.getProfessor ();

    //     if (professor != null)
    //     {
    //         if (professor.getId () != null)
    //         {
    //             try {
    //                 professor = professorService.showProfessorById (professor.getId ());    
    //                 course.setProfessor (professor);
    //             } catch (NoSuchElementException n) {
    //                 return ResponseEntity
    //                 .status(HttpStatus.NOT_ACCEPTABLE)
    //                 .body(String.format("Professor with ID '%s' doesn't exist", professor.getId ()));        
    //             }
    //         } else {
    //             String professorName = professor.getName ();
    //             professor = professorService.showProfessorByName (professorName);
                
    //             if (professor != null) {
    //                 course.setProfessor (professor);
    //             } else {
    //                 return ResponseEntity
    //                 .status(HttpStatus.NOT_ACCEPTABLE)
    //                 .body(String.format("Professor with name '%s' doesn't exist", professorName));
    //             }
    //         }
    //         courseService.addCourse (course);            
    //         return ResponseEntity.ok (course.toString ());
    //     } else {
    //         return ResponseEntity
    //         .status(HttpStatus.NOT_ACCEPTABLE)
    //         .body("'professor' field cannot be kept empty or null");
    //     }
        
    // }

    // @RequestMapping (method = RequestMethod.POST, value = "/data/courses")
    // public ResponseEntity<String> addCourses (@RequestBody List<@Valid Course> courses) {
        
    //     ResponseEntity<String> response;
        
    //     for (Course course : courses) {
    //         response = addCourse (course);
    //         if (response.getStatusCode () != HttpStatus.OK)
    //             return response;            
    //     }    

    //     return ResponseEntity.ok (courses.toString ());
    
    // }

    // Mappings for PUT calls to course service
    
    @RequestMapping (method = RequestMethod.PUT, value = "/data/course")
    public ResponseEntity<String> updateCourse (@Valid @RequestBody Course course) {
        
        Course oldDetails = courseService.showCourseByName (course.getName ());
        if (oldDetails != null)
        {
            course.setStudents (oldDetails.getStudents ());
            courseService.updateCourse (course);            
            return ResponseEntity.ok (course.toString ());    
        } else {
            return ResponseEntity
                .status (HttpStatus.NOT_FOUND)
                .body (
                    String
                    .format(
                        "Couldn't find a course with the name '%s'", 
                        course.getName ()
                    )
                );
        }

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
