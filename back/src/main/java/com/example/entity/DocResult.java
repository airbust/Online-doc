package com.example.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class DocResult {
    private File file; //文档数据
    private Role role; //文档对应权限表
    private Map<String, Object> map; //token

    public DocResult(File file, Role role, Map<String, Object> map) {
        this.file = file;
        this.role = role;
        this.map = map;
    }
}
