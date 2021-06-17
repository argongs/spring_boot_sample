package com.college.data.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Course {
    
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int credits;

    public Course () {}

    public Course (String id, String name, int credits) {
        this.id = id;
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
            boolean idIsAbsent = id.isEmpty () || id.isBlank ();
            boolean nameIsAbsent = name.isEmpty () || name.isBlank ();
            boolean creditIsInvalid = credits <= 0;
            
            if (idIsAbsent || nameIsAbsent || creditIsInvalid)
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

    public int getCredits () {
        return credits;
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
}
