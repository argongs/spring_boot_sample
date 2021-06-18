package com.college.data.teaches;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.college.data.course.Course;
import com.college.data.professor.Professor;

@Entity
public class Teaches {  

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne (fetch = FetchType.EAGER)
    private Professor professor;
    
    @OneToOne (fetch = FetchType.EAGER)
    private Course course;

    public Teaches () {}

    public Teaches (Professor professor, Course course) {
        this.professor = professor;
        this.course = course;
    }

    @Override
    public String toString () {
        return String.format("{'professor' : %s, 'course' : %s}", professor, course);
    }
}
