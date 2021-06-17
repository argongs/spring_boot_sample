package com.college.data.takes;

import java.util.List;
import java.util.NoSuchElementException;

import com.college.data.course.Course;
import com.college.data.course.CourseRepository;
import com.college.data.student.Student;
import com.college.data.student.StudentRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // Necessary for handling the delete operations in the TakesController class
public class TakesService {
    
    @Autowired
    private TakesRepository takesRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

    // Create Methods

    public int addTakes (String studentInfo, String courseInfo) {
        
        Student student;
        Course course;
        Takes takes;

        // Look up the student
        try {
            student = studentRepository.findById (studentInfo).orElseThrow ();
        } catch (NoSuchElementException e1) {
            student = studentRepository.findByName (studentInfo);
        }

        // Look up the course
        try {
            course = courseRepository.findById (courseInfo).orElseThrow();
        } catch (NoSuchElementException e2) {
            course = courseRepository.findByName (courseInfo);
        }

        // Hook em' up if they are valid 
        if (student == null || student.isInvalid ())
            return 1; 
        else if (course == null || course.isInvalid ())
            return 2;

        takes = new Takes (student, course);
        takesRepository.save (takes);
        
        return 0;
    }

    // Read Methods

    public List<Takes> showTakes () {
        List <Takes> takes = new ArrayList <Takes> ();
        
        for (Takes c : takesRepository.findAll()) {
            takes.add(c);
        }

        return takes;
    }

    public List<Takes> showTakesByStudentName (String name) {
        return takesRepository.findAllByStudentName(name);
    }

    public List<Takes> showTakesByCourseName (String name) {
        return takesRepository.findAllByCourseName(name);
    }

    // Delete Methods
    
    public void deleteTakesByCourseName (String courseName) {
        takesRepository.deleteByCourseName (courseName);
    }

    public void deleteTakesByStudentName (String takesName) {
        takesRepository.deleteByStudentName (takesName);
    }

    public void deleteTakesByStudentNameAndCourseName (String studentName, String courseName) {
        takesRepository.deleteByStudentNameAndCourseName (studentName, courseName);
    }

}
