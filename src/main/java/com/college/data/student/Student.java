package com.college.data.student;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Student {
    
    @Id
    private String id;
    private String name;
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

    public boolean isInvalid () {
        
        try {
            boolean idIsAbsent = id.isEmpty () || id.isBlank ();
            boolean nameIsAbsent = name.isEmpty () || name.isBlank ();
            boolean semesterIsInvalid = semester <= 0;
            
            if (idIsAbsent || nameIsAbsent || semesterIsInvalid)
                return true;
            else
                return false;
        
        } catch (NullPointerException n) {
            return true;
        }
        
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
