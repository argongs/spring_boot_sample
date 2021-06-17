package com.college.data.student;

import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, String> {
    
    public Student findByName (String name);
    public void deleteByName (String name);

}
