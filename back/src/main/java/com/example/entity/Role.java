package com.example.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Role {

    private Integer id;//角色id
    private String name;//角色名

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }}
