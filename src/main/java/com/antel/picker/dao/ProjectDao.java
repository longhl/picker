package com.antel.picker.dao;

import com.antel.picker.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ProjectDao {

    void insert(Project project);

    void insertProjectInstRel(Project project);

    Project getLatestProject();

    Project getById(@Param("id") Integer id);

    List<Project> selectAll();

    void delProjectById(@Param("id") Integer id);

    List<Project> getByName(String name);
}
