package com.college.data.course;

import java.util.List;
import java.util.NoSuchElementException;
import java.lang.NullPointerException;

import com.college.data.student.Student;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // Necessary for handling the delete operations in the CourseController class
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;

    // Create Methods

    public void addCourse (Course course) {
        courseRepository.save(course);
    }

    // Read Methods

    public List<Course> showCourses () {
        List <Course> courses = new ArrayList <Course> ();
        
        for (Course c : courseRepository.findAll()) {
            courses.add(c);
        }

        return courses;
    }

    public Course showCourseById (String courseId) throws NoSuchElementException{
        return courseRepository.findById(courseId).orElseThrow();
    }

    public Course showCourseByName (String courseName) {
        return courseRepository.findByName(courseName);
    }

    public List<Student> showStudentsByCourseId (String courseId) {

        try {
            Course course = showCourseById (courseId);
            return course.getStudents ();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException();
        }

    }

    public List<Student> showStudentsByCourseName (String courseName) {

        try {
            Course course = showCourseByName (courseName);
            return course.getStudents ();
        } catch (NullPointerException n) {
            throw new NoSuchElementException();
        }

    }

    // Update Methods

    public void updateCourse (Course course) {
        courseRepository.save (course);
    }

    // Delete Methods

    public void deleteCourseById (String courseId) {
        courseRepository.deleteById (courseId);
    }
    
    public void deleteCourseByName (String courseName) {
        courseRepository.deleteByName (courseName);
    }
    
    public void deleteCourseByProfessorName (String professorName) {
        courseRepository.deleteAllByProfessorName (professorName);
    }

}
