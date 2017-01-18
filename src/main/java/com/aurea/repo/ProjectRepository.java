package com.aurea.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.aurea.model.Project;

@Service
public interface ProjectRepository extends CrudRepository<Project, Long>{

}
