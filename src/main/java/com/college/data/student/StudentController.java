package com.college.data.student;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.college.data.course.Course;
import com.college.data.course.CourseService;
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
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;
    private Logger logger;

    private List<Course> loadCourseDetails (List<Course> optedCourses) {
        Course course;
        List<Course> finalisedCourses = new LinkedList<Course>();
        for(Course optedCourse : optedCourses) {
            course = courseService.showCourseByName(optedCourse.getName ());
            if (course != null)
                finalisedCourses.add(course);
        }
        return finalisedCourses;
    }

    public StudentController () {
        logger = LoggerFactory.getLogger(StudentController.class);
    }

    // Mappings for GET calls to student service

    @RequestMapping ("/data/students/{student_name_or_id}")
    public Student showStudent (
        @PathVariable ("student_name_or_id")  
        String studentNameOrId
    ) {
        
        Student student;
        
        try {
            // Assume 'studentNameOrId' to represent student id, intially
            student = studentService.showStudentById (studentNameOrId);    
        } catch (NoSuchElementException e1) {
            try {
                // Once student id assumption fails, then assume 'studentNameOrId' to be student name
                student = studentService.showStudentByName (studentNameOrId);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                student = null;
            }
        }

        return student;
    }

    @RequestMapping ("/data/students")
    public List<Student> showStudents () {
        return studentService.showStudents();
    }

    @RequestMapping ("/data/students/{student_id_or_name}/courses")
    public List<Course> showCoursesByStudentIdOrName (
        @PathVariable ("student_id_or_name")
        String studentIdOrName
    ) {
        List<Course> courses;
        
        try {
            // Assume 'studentIdOrName' to represent student id, intially
            courses = studentService.showCoursesByStudentId (studentIdOrName);    
        } catch (NoSuchElementException e1) {
            try {
                // Once student id assumption fails, then assume 'studentIdOrName' to be student name
                courses = studentService.showCoursesByStudentName (studentIdOrName);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                courses = null;
            }
        }
        
        return courses;
    }


    // Mappings for POST calls to student service
    
    @RequestMapping (method = RequestMethod.POST, value = "/data/student")
    public ResponseEntity<String> addStudent (@Valid @RequestBody Student student) {

        if (student.getCourses () != null) {
            List<Course> optedCourses = student.getCourses ();
            List<Course> finalisedCourses = loadCourseDetails(optedCourses);
            student.setCourses(finalisedCourses);
        }
        
        studentService.addStudent (student);            
        return ResponseEntity.ok (student.toString ());
    
    }

    @RequestMapping (method = RequestMethod.POST, value = "/data/students")
    public ResponseEntity<String> addStudents (@RequestBody List<@Valid Student> students) {
        
        for (Student student : students) {
                studentService.addStudent (student);            
        }    

        return ResponseEntity.ok (students.toString ());
    
    }

    // Mappings for PUT calls to student service
    
    @RequestMapping (method = RequestMethod.PUT, value = "/data/student/{student_id_or_name}")
    public ResponseEntity<String> updateStudent (
        @Valid 
        @PathVariable ("student_id_or_name") String studentIdOrName,
        @RequestBody Student newDetails
    ) {
        
        Student oldDetails = showStudent (studentIdOrName);
        if (oldDetails != null)
        {
            List<Course> optedCourses = newDetails.getCourses ();
            List<Course> finalisedCourses = loadCourseDetails(optedCourses);
            newDetails.setCourses(finalisedCourses);

            Student updatedDetails = new Student (oldDetails, newDetails);
            studentService.updateStudent (updatedDetails);
            return ResponseEntity.ok (updatedDetails.toString ());    
        } else {
            return ResponseEntity
                .status (HttpStatus.NOT_FOUND)
                .body (
                    String
                    .format(
                        "Couldn't find a student with the name/id '%s'", 
                        studentIdOrName
                    )
                );
        }

    }

    // Mappings for DELETE calls to student service

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/students/{student_name_or_id}")
    public void deleteStudent (
        @PathVariable ("student_name_or_id")
        String studentNameOrId 
    ) {
        
        try {
            // Assume 'studentNameOrId' to represent student id, intially
            studentService.deleteStudentById (studentNameOrId);    
        } catch (EmptyResultDataAccessException e1) {
            try {
                // Once student id assumption fails, then assume 'studentNameOrId' to be student name
                studentService.deleteStudentByName (studentNameOrId);
            } catch (EmptyResultDataAccessException e2) {
                // Once both the assumptions fail, then simply return
                System.out.println ("Failed to delete!");
                return;
            }
        }

    }

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/students")
    public void deleteStudents (
        @RequestBody 
        List<@NotBlank @NotNull String> studentNameOrIds
    ) {

        for (String studentNameOrId : studentNameOrIds)
            deleteStudent (studentNameOrId);

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