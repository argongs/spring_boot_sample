package com.college.data.professor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Professor {
    
    @Id
    private String id;
    private String name;
    private int salary;

    public Professor () {}

    public Professor (String id, String name, int salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString () {
        String output = String.format("{'id' : %s, 'name' : %s, 'salary' : %d}", id, name, salary);
        return output;
    }

    public boolean isInvalid () {
        
        try {
            boolean idIsAbsent = id.isEmpty () || id.isBlank ();
            boolean nameIsAbsent = name.isEmpty () || name.isBlank ();
            boolean salaryIsInvalid = salary <= 0;
            
            if (idIsAbsent || nameIsAbsent || salaryIsInvalid)
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

    public int getSalary () {
        return salary;
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
}
