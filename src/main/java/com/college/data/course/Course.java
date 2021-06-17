package com.college.data.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Course {
    
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;
    @Column (nullable = false)
    private String name;
    @Column (nullable = false)
    private int credits;

    public Course () {}

    public Course (String name, int credits) {
        this.name = name;
        this.credits = credits;
    }
    
    @Override
    public String toString () {
        String output = String.format("{'id' : %s, 'name' : %s, 'credits' : %d}", id, name, credits);
        return output;
    }

    public boolean isInvalid () {
        
        try {
            boolean nameIsAbsent = name.isEmpty () || name.isBlank ();
            boolean creditIsInvalid = credits <= 0;
            
            if (nameIsAbsent || creditIsInvalid)
                return true;
            else
                return false;
        
        } catch (NullPointerException n) {
            return true;
        }
        
    }

    // Getters
    public int getId () {
        return id;
    }

    public String getName () {
        return name;
    }

    public int getCredits () {
        return credits;
    }


    // Setters
    public void setName (String name) {
        this.name = name;
    }

    public void setCredits (int credits) {
        this.credits = credits;
    }
}
