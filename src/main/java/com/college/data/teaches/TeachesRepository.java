package com.college.data.teaches;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TeachesRepository extends CrudRepository<Teaches, Integer> {
    public List<Teaches> findAllByProfessorName (String name);
    public List<Teaches> findAllByCourseName (String name);
    public void deleteByProfessorName (String name);
    public void deleteByCourseName (String name);
    public void deleteByProfessorNameAndCourseName (String professorName, String courseName);
}
