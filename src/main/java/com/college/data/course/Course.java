package com.college.data.course;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.college.data.professor.Professor;
import com.college.data.student.Student;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Course {
    
    @Id
    @GeneratedValue (generator = "system-uuid")
    @GenericGenerator (name = "system-uuid", strategy = "uuid")
    private String id;
    @Column (nullable = false)
    private String name;
    @Column (nullable = false)
    private int credits;

    // Foreign key mappings
    @ManyToOne
    @JoinColumn (name = "professor_id", nullable = false)
    @JsonBackReference
    private Professor professor;
    @ManyToMany (mappedBy = "courses")
    @JsonIgnore
    private List<Student> students;

    public Course () {}

    public Course (Course oldDetails, Course newDetails) {
        this.id = oldDetails.getId ();
        this.name = newDetails.getName () == null ? oldDetails.getName () : newDetails.getName ();
        this.credits = newDetails.getCredits () == 0 ? oldDetails.getCredits () : newDetails.getCredits ();
        this.professor = oldDetails.getProfessor ();
        this.students = oldDetails.getStudents ();
    }

    public Course (String id, String name, int credits, Professor professor) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.professor = professor;
    }
    
    @Override
    public String toString () {
        String output = String
            .format(
                "{'id' : %s, 'name' : %s, 'credits' : %d}", 
                id, name, credits
            );
        return output;
    }

    // Getters
    public String getId () {
        return id;
    }

    public String getName () {
        return name;
    }

    public int getCredits () {
        return credits;
    }

    public Professor getProfessor () {
        return professor;
    }

    public List<Student> getStudents () {
        return students;
    }

    // Setters
    public void setId (String id) {
        this.id = id;
    }
    
    public void setName (String name) {
        this.name = name;
    }

    public void setCredits (int credits) {
        this.credits = credits;
    }

    public void setProfessor (Professor professor) {
        this.professor = professor;
    }

    public void setStudents (List<Student> students) {
        this.students = students;
    }
}