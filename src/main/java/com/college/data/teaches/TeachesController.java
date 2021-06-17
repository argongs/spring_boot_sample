package com.college.data.teaches;

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
public class TeachesController {
    
    @Autowired
    private TeachesService teachesService;
    private Logger logger;

    public TeachesController () {
        logger = LoggerFactory.getLogger(TeachesController.class);
    }

    // Mappings for GET calls to teaches service

    @RequestMapping ("/data/teaches")
    public List<Teaches> showTeaches () {
        List<Teaches> teachesList = teachesService.showTeaches (); 
        System.out.println(teachesList);
        return teachesList;
    }

    @RequestMapping ("/data/teaches/{teaches_info}")
    public List<Teaches> showTeaches (
        @PathVariable ("teaches_info") 
        @NotBlank 
        @NotNull 
        String teachesInfo
    ) {
        
        List<Teaches> teaches;
        
        try {
            // Assume 'teachesInfo' to represent teaches a professor's name, intially
            teaches = teachesService.showTeachesByProfessorName (teachesInfo);    
        } catch (NoSuchElementException e1) {
            try {
                // Once the above assumption fails, then assume 'teachesInfo' to be a course name
                teaches = teachesService.showTeachesByCourseName (teachesInfo);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                teaches = null;
            }
        }
        System.out.println(teaches);
        return teaches;
    }

    // Mappings for POST calls to teaches service
    
    @RequestMapping (method = RequestMethod.POST, value = "/data/teaches")
    public ResponseEntity<String> addTeaches (@RequestBody List<String> professorAndCourseInfo) {
        
        if (professorAndCourseInfo.size () != 2)
            return new ResponseEntity<String> (
                "Invalid input", 
                HttpStatus.BAD_REQUEST
            );
        else {
            String professorInfo = professorAndCourseInfo.get (0);
            String courseInfo = professorAndCourseInfo.get (1);
            int returnStatus = teachesService.addTeaches (professorInfo, courseInfo);

            switch (returnStatus) {
                case 0:
                    return ResponseEntity.ok ("Valid input");    
                case 1:
                    return new ResponseEntity<String> (
                        String.format("Requested professor '%s' doesn't exist", professorInfo), 
                        HttpStatus.BAD_REQUEST
                    );
                case 2:
                    return new ResponseEntity<String> (
                        String.format("Requested course %s doesn't exist", courseInfo), 
                        HttpStatus.BAD_REQUEST
                    );
                default:
                    return new ResponseEntity<String> (
                        String.format(
                            "Rogue return code found. Server side code is corrupted!", 
                            courseInfo
                        ), 
                        HttpStatus.INTERNAL_SERVER_ERROR
                    );
            }
                
        }

    }

    @RequestMapping (method = RequestMethod.POST, value = "/data/multiple_teaches")
    public ResponseEntity<String> addMultipleTeaches (@RequestBody List<List<String>> professorAndCourseInfoList) {
        
        ResponseEntity<String> response;

        for (List<String> professorAndCourseInfo : professorAndCourseInfoList) {
            response = addTeaches(professorAndCourseInfo);
            if (response.getStatusCode() != HttpStatus.OK)
                return response;    
        }    

        return ResponseEntity.ok ("Valid input");
    }

    // Mappings for DELETE calls to teaches service

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/teaches/{teaches_info}")
    public void deleteTeaches (
        @PathVariable ("teaches_info") 
        @NotBlank 
        @NotNull 
        String teachesInfo 
    ) {
        
        try {
            // Assume 'teachesInfo' to represent teaches id, intially
            teachesService.deleteTeachesByProfessorName (teachesInfo);    
        } catch (EmptyResultDataAccessException e1) {
            try {
                // Once teaches id assumption fails, then assume 'teachesInfo' to be teaches name
                teachesService.deleteTeachesByCourseName (teachesInfo);
            } catch (EmptyResultDataAccessException e2) {
                // Once both the assumptions fail, then simply return
                System.out.println ("Failed to delete!");
                return;
            }
        }

    }

    @RequestMapping (
        method = RequestMethod.DELETE, 
        value = "/data/teaches/{professor_name}/{course_name}"
    )
    public void deleteTeachesByProfessorNameAndCourseName (
        @PathVariable ("professor_name") String professorName,
        @PathVariable ("course_name") String courseName
    ) {
        teachesService.deleteTeachesByProfessorNameAndCourseName(professorName, courseName);
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
