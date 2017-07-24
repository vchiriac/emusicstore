package com.emusicstore.service;

import com.emusicstore.model.User;

import java.util.List;

public interface UserService {

    User findById(int id);

    User findByName(String name);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUserBySSO(String sso);

    List<User> findAllUsers();

}