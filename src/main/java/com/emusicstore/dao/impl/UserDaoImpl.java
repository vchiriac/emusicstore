package com.emusicstore.dao.impl;

import com.emusicstore.dao.AbstractDao;
import com.emusicstore.dao.UserDao;
import com.emusicstore.model.User;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("userDao")
@Transactional
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {

    static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User findById(int id) {
        User user = getByKey(id);
        if (user != null) {
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }

    @Override
    public User findByName(String name) {
        logger.info("name : {}", name);
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("username", name));
        User user = (User) crit.uniqueResult();
        if (user != null) {
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }

    @Override
    public void save(User user) {
        persist(user);
    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }
}
