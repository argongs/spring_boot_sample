package com.college.data.professor;

import org.springframework.data.repository.CrudRepository;

public interface ProfessorRepository extends CrudRepository<Professor, String> {
    
    public Professor findByName (String name);
    public void deleteByName (String name);

}
