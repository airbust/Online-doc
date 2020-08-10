package com.example.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class User implements Serializable {

    private Integer id;
    private String name;
    private String password;
    private String mail;

}
