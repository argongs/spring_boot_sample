package com.college.data.takes;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TakesRepository extends CrudRepository<Takes, Integer> {
    
    public List<Takes> findAllByStudentName (String name);
    public List<Takes> findAllByCourseName (String name);
    public void deleteByStudentName (String name);
    public void deleteByCourseName (String name);
    public void deleteByStudentNameAndCourseName (String studentName, String courseName);

}
