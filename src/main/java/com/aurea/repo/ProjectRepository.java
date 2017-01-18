package com.aurea.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aurea.model.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
    
}
