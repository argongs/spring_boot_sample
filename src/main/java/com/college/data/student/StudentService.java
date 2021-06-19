package com.college.data.student;

import java.util.List;
import java.util.NoSuchElementException;

import com.college.data.course.Course;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // Necessary for handling the delete operations in the StudentController class
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;

    // Create Methods

    public void addStudent (Student student) {
        studentRepository.save(student);
    }

    // Read Methods

    public List<Student> showStudents () {
        List <Student> students = new ArrayList <Student> ();
        
        for (Student c : studentRepository.findAll()) {
            students.add(c);
        }

        return students;
    }

    public Student showStudentById (String studentId) {
        return studentRepository.findById(studentId).orElseThrow();
    }

    public Student showStudentByName (String studentName) {
        return studentRepository.findByName(studentName);
    }

    public List<Course> showCoursesByStudentId (String studentId) throws NoSuchElementException {

        try {
            Student student = showStudentById (studentId);
            return student.getCourses ();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException();
        }
    }

    public List<Course> showCoursesByStudentName (String studentName) throws NoSuchElementException {
       
        try {
            Student student = showStudentByName (studentName);
            return student.getCourses ();    
        } catch (NullPointerException e) {
            throw new NoSuchElementException();
        }
        
    }

    // Update Methods

    public void updateStudent (Student students) {
        studentRepository.save(students);
    }

    // Delete Methods

    public void deleteStudentById (String studentId) {
        studentRepository.deleteById (studentId);
    }

    public void deleteStudentByName (String studentName) {
        studentRepository.deleteByName (studentName);
    }

}
