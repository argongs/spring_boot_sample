package com.college.data.student;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.college.data.course.Course;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Student {
    
    @Id
    @GeneratedValue (generator = "system-uuid")
    @GenericGenerator (name = "system-uuid", strategy = "uuid")
    private String id;
    @Column (nullable = false)
    private String name;
    @Column (nullable = false)
    private int semester;

    // Foreign key mapping
    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Course> courses;

    public Student () {}

    public Student (String id, String name, int semester, List<Course> courses) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.courses = courses;
    }
    
    @Override
    public String toString () {
        String output = String
            .format(
                "{'id' : %s, 'name' : %s, 'semester' : %d, 'courses' : %s}"
                , id, name, semester, courses
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

    public int getSemester () {
        return semester;
    }

    public List<Course> getCourses () {
        return courses;
    }

    // Setters
    public void setId (String id) {
        this.id = id;
    }
    
    public void setName (String name) {
        this.name = name;
    }

    public void setSemester (int semester) {
        this.semester = semester;
    }

    public void setCourses (List<Course> courses) {
        this.courses = courses;
    }
}
