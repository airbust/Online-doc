package com.example.service;

import com.example.dao.TemplateDao;
import com.example.entity.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {
    @Autowired
    TemplateDao templateDao;

    public List<Template> getAll() {
        return templateDao.getAll();
    }

    public Template getById(Integer id){
        return templateDao.getById(id);
    }
}
