package com.college.data.student;

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
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    private Logger logger;

    public StudentController () {
        logger = LoggerFactory.getLogger(StudentController.class);
    }

    // Mappings for GET calls to student service

    @RequestMapping ("/data/students")
    public List<Student> showStudents () {
        return studentService.showStudents();
    }

    @RequestMapping ("/data/students/{student_info}")
    public Student showStudent (
        @PathVariable ("student_info") 
        @NotBlank 
        @NotNull 
        String studentInfo
    ) {
        
        Student student;
        
        try {
            // Assume 'studentInfo' to represent student id, intially
            student = studentService.showStudentById (studentInfo);    
        } catch (NoSuchElementException e1) {
            try {
                // Once student id assumption fails, then assume 'studentInfo' to be student name
                student = studentService.showStudentByName (studentInfo);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                student = null;
            }
        }
        
        return student;
    }


    // Mappings for POST calls to student service
    
    @RequestMapping (method = RequestMethod.POST, value = "/data/student")
    public ResponseEntity<String> addStudent (@RequestBody Student student) {
        
        if (student.isInvalid ())
            return new ResponseEntity<String> (
                "Invalid input", 
                HttpStatus.BAD_REQUEST
            );
        else {
            studentService.addStudent (student);            
            return ResponseEntity.ok ("Valid input");
        }

    }

    @RequestMapping (method = RequestMethod.POST, value = "/data/students")
    public ResponseEntity<String> addStudents (@RequestBody List<Student> students) {
        
        for (Student student : students) {
            if (student.isInvalid ())
                return new ResponseEntity<String> (
                    "Invalid input", 
                    HttpStatus.BAD_REQUEST
                );
            else {
                studentService.addStudent (student);            
            }
        }    

        return ResponseEntity.ok ("Valid input");
    }

    // Mappings for PUT calls to student service
    
    @RequestMapping (method = RequestMethod.PUT, value = "/data/student")
    public ResponseEntity<String> updateStudent (@RequestBody Student student) {
        
        if (student.isInvalid ())
            return new ResponseEntity<String> (
                "Invalid input", 
                HttpStatus.BAD_REQUEST
            );
        else {
            studentService.updateStudent (student);            
            return ResponseEntity.ok ("Valid input");
        }

    }

    @RequestMapping (method = RequestMethod.PUT, value = "/data/students")
    public ResponseEntity<String> updateStudents (@RequestBody List<Student> students) {

        for (Student student : students) {
            if (student.isInvalid ())
                return new ResponseEntity<String> (
                    "Invalid input", 
                    HttpStatus.BAD_REQUEST
                );
            else {
                studentService.updateStudent (student);
            }
        }    
               
        return ResponseEntity.ok ("Valid input");
    }

    // Mappings for DELETE calls to student service

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/students/{student_info}")
    public void deleteStudent (
        @PathVariable ("student_info") 
        @NotBlank 
        @NotNull 
        String studentInfo 
    ) {
        
        try {
            // Assume 'studentInfo' to represent student id, intially
            studentService.deleteStudentById (studentInfo);    
        } catch (EmptyResultDataAccessException e1) {
            try {
                // Once student id assumption fails, then assume 'studentInfo' to be student name
                studentService.deleteStudentByName (studentInfo);
            } catch (EmptyResultDataAccessException e2) {
                // Once both the assumptions fail, then simply return
                System.out.println ("Failed to delete!");
                return;
            }
        }

    }

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/students")
    public void deleteStudents (@RequestBody List<String> studentInfos) {

        for (String studentInfo : studentInfos)
            deleteStudent (studentInfo);

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
