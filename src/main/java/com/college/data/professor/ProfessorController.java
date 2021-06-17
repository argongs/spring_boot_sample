package com.college.data.professor;

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

    @RequestMapping ("/data/professors/{professor_info}")
    public Professor showProfessor (
        @PathVariable ("professor_info") 
        @NotBlank 
        @NotNull 
        String professorInfo
    ) {
        
        Professor professor;
        
        try {
            // Assume 'professorInfo' to represent professor id, intially
            professor = professorService.showProfessorById (professorInfo);    
        } catch (NoSuchElementException e1) {
            try {
                // Once professor id assumption fails, then assume 'professorInfo' to be professor name
                professor = professorService.showProfessorByName (professorInfo);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                professor = null;
            }
        }
        
        return professor;
    }


    // Mappings for POST calls to professor service
    
    @RequestMapping (method = RequestMethod.POST, value = "/data/professor")
    public ResponseEntity<String> addProfessor (@RequestBody Professor professor) {
        
        if (professor.isInvalid ())
            return new ResponseEntity<String> (
                "Invalid input", 
                HttpStatus.BAD_REQUEST
            );
        else {
            professorService.addProfessor (professor);            
            return ResponseEntity.ok ("Valid input");
        }

    }

    @RequestMapping (method = RequestMethod.POST, value = "/data/professors")
    public ResponseEntity<String> addProfessors (@RequestBody List<Professor> professors) {
        
        for (Professor professor : professors) {
            if (professor.isInvalid ())
                return new ResponseEntity<String> (
                    "Invalid input", 
                    HttpStatus.BAD_REQUEST
                );
            else {
                professorService.addProfessor (professor);            
            }
        }    

        return ResponseEntity.ok ("Valid input");
    }

    // Mappings for PUT calls to professor service
    
    @RequestMapping (method = RequestMethod.PUT, value = "/data/professor")
    public ResponseEntity<String> updateProfessor (@RequestBody Professor professor) {
        
        if (professor.isInvalid ())
            return new ResponseEntity<String> (
                "Invalid input", 
                HttpStatus.BAD_REQUEST
            );
        else {
            professorService.updateProfessor (professor);            
            return ResponseEntity.ok ("Valid input");
        }

    }

    @RequestMapping (method = RequestMethod.PUT, value = "/data/professors")
    public ResponseEntity<String> updateProfessors (@RequestBody List<Professor> professors) {

        for (Professor professor : professors) {
            if (professor.isInvalid ())
                return new ResponseEntity<String> (
                    "Invalid input", 
                    HttpStatus.BAD_REQUEST
                );
            else {
                professorService.updateProfessor (professor);
            }
        }    
               
        return ResponseEntity.ok ("Valid input");
    }

    // Mappings for DELETE calls to professor service

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/professors/{professor_info}")
    public void deleteProfessor (
        @PathVariable ("professor_info") 
        @NotBlank 
        @NotNull 
        String professorInfo 
    ) {
        
        try {
            // Assume 'professorInfo' to represent professor id, intially
            professorService.deleteProfessorById (professorInfo);    
        } catch (EmptyResultDataAccessException e1) {
            try {
                // Once professor id assumption fails, then assume 'professorInfo' to be professor name
                professorService.deleteProfessorByName (professorInfo);
            } catch (EmptyResultDataAccessException e2) {
                // Once both the assumptions fail, then simply return
                System.out.println ("Failed to delete!");
                return;
            }
        }

    }

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/professors")
    public void deleteProfessors (@RequestBody List<String> professorInfos) {

        for (String professorInfo : professorInfos)
            deleteProfessor (professorInfo);

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
