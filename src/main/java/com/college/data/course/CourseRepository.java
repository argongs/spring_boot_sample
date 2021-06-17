package com.college.data.course;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, String>{
    
    public Course findByName (String name);
    public void deleteByName (String name);
    
}
