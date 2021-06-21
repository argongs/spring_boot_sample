package com.college.data.professor;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.college.data.course.Course;
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
public class ProfessorController {
    
    @Autowired
    private ProfessorService professorService;
    private Logger logger;

    public ProfessorController () {
        logger = LoggerFactory.getLogger(ProfessorController.class);
    }

    // Mappings for GET calls to professor service

    @RequestMapping ("/data/professors")
    public List<Professor> showProfessors () {
        return professorService.showProfessors();
    }

    @RequestMapping ("/data/professors/{professor_name_or_id}")
    public Professor showProfessor (
        @PathVariable ("professor_name_or_id")  
        String professorNameOrId
    ) {
        
        Professor professor;
        
        try {
            // Assume 'professorNameOrId' to represent professor id, intially
            professor = professorService.showProfessorById (professorNameOrId);    
        } catch (NoSuchElementException e1) {
            try {
                // Once professor id assumption fails, then assume 'professorNameOrId' to be professor name
                professor = professorService.showProfessorByName (professorNameOrId);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                professor = null;
            }
        }
        return professor;
    
    }

    @RequestMapping ("/data/professors/{professor_id_or_name}/courses")
    public List<Course> showCoursesByProfessorIdOrName (
        @PathVariable ("professor_id_or_name") 
        String professorIdOrName
    ) {
        List<Course> courses;
        
        try {
            // Assume 'professorIdOrName' to represent professor id, intially
            courses = professorService.showCoursesByProfessorId (professorIdOrName);    
        } catch (NoSuchElementException e1) {
            try {
                // Once professor id assumption fails, then assume 'professorIdOrName' to be professor name
                courses = professorService.showCoursesByProfessorName (professorIdOrName);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                courses = null;
            }
        }
        
        return courses;
    }

    // Mappings for POST calls to professor service
    
    @RequestMapping (method = RequestMethod.POST, value = "/data/professor")
    public ResponseEntity<String> addProfessor (@Valid @RequestBody Professor professor) {

        professorService.addProfessor (professor);       
        return ResponseEntity.ok (professor.toString ());
    
    }

    @RequestMapping (method = RequestMethod.POST, value = "/data/professors")
    public ResponseEntity<String> addProfessors (@RequestBody List<@Valid Professor> professors) {
        
        for (Professor professor : professors) {
                professorService.addProfessor (professor);            
        }    

        return ResponseEntity.ok (professors.toString ());
    
    }

    // Mappings for PUT calls to professor service
    
    @RequestMapping (method = RequestMethod.PUT, value = "/data/professor/{professor_id_or_name}")
    public ResponseEntity<String> updateProfessor (
        @Valid 
        @PathVariable ("professor_id_or_name") String professorIdOrName,
        @RequestBody Professor newDetails) {
        
        Professor oldDetails = showProfessor (professorIdOrName);
        if (oldDetails != null)
        {
            Professor updatedDetails = new Professor (oldDetails, newDetails);
            professorService.updateProfessor (updatedDetails);
            return ResponseEntity.ok (updatedDetails.toString ());    
        } else {
            return ResponseEntity
                .status (HttpStatus.NOT_FOUND)
                .body (
                    String
                    .format(
                        "Couldn't find a professor with the name/id '%s'", 
                        professorIdOrName
                    )
                );
        }

    }

    // Mappings for DELETE calls to professor service

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/professors/{professor_name_or_id}")
    public void deleteProfessor (
        @PathVariable ("professor_name_or_id")
        String professorNameOrId 
    ) {
        
        try {
            // Assume 'professorNameOrId' to represent professor id, intially
            professorService.deleteProfessorById (professorNameOrId);    
        } catch (EmptyResultDataAccessException e1) {
            try {
                // Once professor id assumption fails, then assume 'professorNameOrId' to be professor name
                professorService.deleteProfessorByName (professorNameOrId);
            } catch (EmptyResultDataAccessException e2) {
                // Once both the assumptions fail, then simply return
                System.out.println ("Failed to delete!");
                return;
            }
        }

    }

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/professors")
    public void deleteProfessors (
        @RequestBody 
        List<@NotBlank @NotNull String> professorNameOrIds
    ) {

        for (String professorNameOrId : professorNameOrIds)
            deleteProfessor (professorNameOrId);

    }

    // Exception handlers

    @ExceptionHandler(MismatchedInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMismatchedInputException (
        MismatchedInputException exception
    ) {
        logger.info ("Recieved data with incorrect syntax.");
        return ResponseEntity
            .status (HttpStatus.BAD_REQUEST)
            .body (exception.getMessage());   
    }

    @ExceptionHandler (DataIntegrityViolationException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDataIntegrityViolationException (
        DataIntegrityViolationException exception
    ) {
        logger.info ("Recieved data in clear violation with data integrity.");
        return ResponseEntity
            .status (HttpStatus.BAD_REQUEST)
            .body (exception.getMessage());
    }

    @ExceptionHandler (HttpMessageNotReadableException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException exception
    ) {
        logger.info ("Failed to read the contents of the request.");
        return ResponseEntity
            .status (HttpStatus.BAD_REQUEST)
            .body (exception.getMessage());
    }

}
