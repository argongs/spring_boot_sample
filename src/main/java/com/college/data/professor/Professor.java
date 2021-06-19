package com.college.data.professor;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.college.data.course.Course;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Professor {
    
    @Id
    @GeneratedValue (generator = "system-uuid")
    @GenericGenerator (name = "system-uuid", strategy = "uuid")
    private String id;
    @Column (nullable = false)
    private String name;
    @Column (nullable = false)
    private int salary;

    // Foreign key mappings
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn (name = "professor_fk")
    private List<Course> courses;

    public Professor () {}

    public Professor (String id, String name, int salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString () {
        String output = String
            .format (
                "{'id' : %s, 'name' : %s, 'salary' : %d, 'courses' : %s}"
                , id, name, salary, courses
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

    public int getSalary () {
        return salary;
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

    public void setSalary (int salary) {
        this.salary = salary;
    }

    public void setCourses (List<Course> courses) {
        this.courses = courses;
    }
}
