package com.college.data.takes;

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
public class TakesController {
    
    @Autowired
    private TakesService takesService;
    private Logger logger;

    public TakesController () {
        logger = LoggerFactory.getLogger(TakesController.class);
    }

    // Mappings for GET calls to takes service

    @RequestMapping ("/data/takes")
    public List<Takes> showTakes () {
        List<Takes> takesList = takesService.showTakes (); 
        System.out.println(takesList);
        return takesList;
    }

    @RequestMapping ("/data/takes/{takes_info}")
    public List<Takes> showTakes (
        @PathVariable ("takes_info") 
        @NotBlank 
        @NotNull 
        String takesInfo
    ) {
        
        List<Takes> takes;
        
        try {
            // Assume 'takesInfo' to represent takes a student's name, intially
            takes = takesService.showTakesByStudentName (takesInfo);    
        } catch (NoSuchElementException e1) {
            try {
                // Once the above assumption fails, then assume 'takesInfo' to be a course name
                takes = takesService.showTakesByCourseName (takesInfo);
            } catch (NoSuchElementException e2) {
                // Once both the assumptions fail, then return null
                takes = null;
            }
        }
        System.out.println(takes);
        return takes;
    }

    // Mappings for POST calls to takes service
    
    @RequestMapping (method = RequestMethod.POST, value = "/data/takes")
    public ResponseEntity<String> addTakes (@RequestBody List<String> studentAndCourseInfo) {
        
        if (studentAndCourseInfo.size () != 2)
            return new ResponseEntity<String> (
                "Invalid input", 
                HttpStatus.BAD_REQUEST
            );
        else {
            String studentInfo = studentAndCourseInfo.get (0);
            String courseInfo = studentAndCourseInfo.get (1);
            int returnStatus = takesService.addTakes (studentInfo, courseInfo);

            switch (returnStatus) {
                case 0:
                    return ResponseEntity.ok ("Valid input");    
                case 1:
                    return new ResponseEntity<String> (
                        String.format("Requested student '%s' doesn't exist", studentInfo), 
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

    @RequestMapping (method = RequestMethod.POST, value = "/data/multiple_takes")
    public ResponseEntity<String> addMultipleTakes (@RequestBody List<List<String>> studentAndCourseInfoList) {
        
        ResponseEntity<String> response;

        for (List<String> studentAndCourseInfo : studentAndCourseInfoList) {
            response = addTakes(studentAndCourseInfo);
            if (response.getStatusCode() != HttpStatus.OK)
                return response;    
        }    

        return ResponseEntity.ok ("Valid input");
    }

    // Mappings for DELETE calls to takes service

    @RequestMapping (method = RequestMethod.DELETE, value = "/data/takes/{takes_info}")
    public void deleteTakes (
        @PathVariable ("takes_info") 
        @NotBlank 
        @NotNull 
        String takesInfo 
    ) {
        
        try {
            // Assume 'takesInfo' to represent takes id, intially
            takesService.deleteTakesByStudentName (takesInfo);    
        } catch (EmptyResultDataAccessException e1) {
            try {
                // Once takes id assumption fails, then assume 'takesInfo' to be takes name
                takesService.deleteTakesByCourseName (takesInfo);
            } catch (EmptyResultDataAccessException e2) {
                // Once both the assumptions fail, then simply return
                System.out.println ("Failed to delete!");
                return;
            }
        }

    }

    @RequestMapping (
        method = RequestMethod.DELETE, 
        value = "/data/takes/{student_name}/{course_name}"
    )
    public void deleteTakesByStudentNameAndCourseName (
        @PathVariable ("student_name") String studentName,
        @PathVariable ("course_name") String courseName
    ) {
        takesService.deleteTakesByStudentNameAndCourseName(studentName, courseName);
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
