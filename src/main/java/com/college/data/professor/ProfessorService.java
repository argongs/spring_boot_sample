package com.college.data.professor;

import java.util.List;
import java.util.NoSuchElementException;

import com.college.data.course.Course;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // Necessary for handling the delete operations in the ProfessorController class
public class ProfessorService {
    
    @Autowired
    private ProfessorRepository professorRepository;

    // Create Methods

    public void addProfessor (Professor professor) {
        professorRepository.save(professor);
    }

    // Read Methods

    public List<Professor> showProfessors () {
        List <Professor> professors = new ArrayList <Professor> ();
        
        for (Professor c : professorRepository.findAll ()) {
            professors.add(c);
        }

        return professors;
    }

    public Professor showProfessorById (String professorId) {
        return professorRepository.findById (professorId).orElseThrow ();
    }

    public Professor showProfessorByName (String professorName) {
        return professorRepository.findByName (professorName);
    }

    public List<Course> showCoursesByProfessorId (String professorId) {
    
        try {
            Professor professor = showProfessorById (professorId);    
            return professor.getCourses ();
        } catch (NoSuchElementException n) {
            throw new NoSuchElementException();
        }
        
    }

    public List<Course> showCoursesByProfessorName (String professorName) {
    
        Professor professor = showProfessorByName(professorName);

        try {       
            return professor.getCourses ();
        } catch (NullPointerException n) {
            throw new NoSuchElementException();
        }
        
    }

    // Update Methods

    public void updateProfessor (Professor professors) {
        professorRepository.save(professors);
    }

    // Delete Methods

    public void deleteProfessorById (String professorId) {
        professorRepository.deleteById (professorId);
    }

    public void deleteProfessorByName (String professorName) {
        professorRepository.deleteByName (professorName);
    }

}
