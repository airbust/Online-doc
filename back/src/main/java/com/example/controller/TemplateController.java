package com.example.controller;

import com.example.entity.Result;
import com.example.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    private TemplateService templateService;

    @GetMapping("/getAll")
    public Result getAll(){
        try {
            System.out.println("1212121");
            return Result.create(200, "获取成功",templateService.getAll());
        }
        catch (Exception e) {
            return Result.create(200, "获取失败" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        try {
            return Result.create(200, "获取成功",templateService.getById(id));
        }
        catch (Exception e) {
            return Result.create(200, "获取失败" + e.getMessage());
        }
    }
}
