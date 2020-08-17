package com.example.dao;

import com.example.entity.Template;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TemplateDao {

    List<Template> getAll();
    Template getById(Integer id);
}
