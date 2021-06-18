package com.college.data.teaches;

import java.util.List;
import java.util.NoSuchElementException;

import com.college.data.course.Course;
import com.college.data.course.CourseRepository;
import com.college.data.professor.Professor;
import com.college.data.professor.ProfessorRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // Necessary for handling the delete operations in the TeachesController class
public class TeachesService {
    
    @Autowired
    private TeachesRepository teachesRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private CourseRepository courseRepository;

    // Create Methods

    public int addTeaches (String professorInfo, String courseInfo) {
        
        Professor professor;
        Course course;
        Teaches teaches;

        // Look up the professor
        try {
            professor = professorRepository.findById (professorInfo).orElseThrow ();
        } catch (NoSuchElementException e1) {
            professor = professorRepository.findByName (professorInfo);
        }

        // Look up the course
        try {
            course = courseRepository.findById (courseInfo).orElseThrow();
        } catch (NoSuchElementException e2) {
            course = courseRepository.findByName (courseInfo);
        }

        // Hook em' up if they are valid 
        if (professor == null)
            return 1; 
        else if (course == null)
            return 2;

        teaches = new Teaches (professor, course);
        teachesRepository.save (teaches);
        
        return 0;
    }

    // Read Methods

    public List<Teaches> showTeaches () {
        List <Teaches> teaches = new ArrayList <Teaches> ();
        
        for (Teaches c : teachesRepository.findAll()) {
            teaches.add(c);
        }

        return teaches;
    }

    public List<Teaches> showTeachesByProfessorName (String name) {
        return teachesRepository.findAllByProfessorName(name);
    }

    public List<Teaches> showTeachesByCourseName (String name) {
        return teachesRepository.findAllByCourseName(name);
    }

    // Delete Methods
    
    public void deleteTeachesByCourseName (String courseName) {
        teachesRepository.deleteByCourseName (courseName);
    }

    public void deleteTeachesByProfessorName (String teachesName) {
        teachesRepository.deleteByProfessorName (teachesName);
    }

    public void deleteTeachesByProfessorNameAndCourseName (String professorName, String courseName) {
        teachesRepository.deleteByProfessorNameAndCourseName (professorName, courseName);
    }

}
