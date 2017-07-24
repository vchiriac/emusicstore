package com.emusicstore.dao;

import com.emusicstore.model.Role;

import java.util.List;

public interface RoleDao {

    Role findByName(String name);

    List<Role> findAll();

}
