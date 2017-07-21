package com.emusicstore.service.impl;

import com.emusicstore.dao.UserDao;
import com.emusicstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;

    @Override
    public User findById(int id) {
        return dao.findById(id);
    }

    @Override
    public User findByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public void saveUser(User user) {
        dao.save(user);
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUserBySSO(String sso) {

    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }
}
