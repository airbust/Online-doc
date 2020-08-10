package com.example.dao;

import com.example.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoleDao {

    Role findRoleByName(String name);

    List<Role> findUserRoles(Integer id);

    void saveRole(Integer id);
}
