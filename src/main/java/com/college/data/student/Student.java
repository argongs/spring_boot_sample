package com.college.data.student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Student {
    
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private String id;
    @Column (nullable = false)
    private String name;
    @Column (nullable = false)
    private int semester;

    public Student () {}

    public Student (String id, String name, int semester) {
        this.id = id;
        this.name = name;
        this.semester = semester;
    }
    
    @Override
    public String toString () {
        String output = String.format("{'id' : %s, 'name' : %s, 'semester' : %d}", id, name, semester);
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
}
