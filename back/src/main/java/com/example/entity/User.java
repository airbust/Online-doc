package com.example.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class User implements Serializable {

    private Integer id;
    private String name;
    private String password;
    private String mail;
    private String avatar;
    private String gender;
    private String birth;
    private String job;
    private String info;

}
