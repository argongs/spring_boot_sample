package com.college.data.takes;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.college.data.course.Course;
import com.college.data.student.Student;

@Entity
public class Takes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Student student;
    
    @ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Course course;

    public Takes () {}

    public Takes (Student student, Course course) {

        this.student = student;
        this.course = course;
    
    }

    @Override
    public String toString () {
        return String.format("{'student' : %s, 'course' : %s}", student, course);
    }
}