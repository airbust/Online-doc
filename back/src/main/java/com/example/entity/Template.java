package com.example.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Template {
    private Integer id;
    private String name;
    private String body;
    private String cover;

}
