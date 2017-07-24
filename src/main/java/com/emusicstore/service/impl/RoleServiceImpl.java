package com.emusicstore.service.impl;

import com.emusicstore.dao.RoleDao;
import com.emusicstore.model.Role;
import com.emusicstore.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;


    @Override
    public Role findByName(final String name) {
        return roleDao.findByName(name);
    }
}
