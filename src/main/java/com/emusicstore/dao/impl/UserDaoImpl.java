package com.emusicstore.dao.impl;

import com.emusicstore.dao.AbstractDao;
import com.emusicstore.dao.UserDao;
import com.emusicstore.model.User;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository("userDao")
@Transactional
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findById(int id) {
        User user = entityManager.find(User.class, id);
        //User user = getByKey(id);
        if (user.getRoles() == null) {
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }

    @Override
    public User findByName(String name) {
        logger.info("name : {}", name);
        User user = null;
        Query query = entityManager.createQuery("select u from User u where u.username = :username").setParameter("username", name);
        //Criteria crit = createEntityCriteria();
        //crit.add(Restrictions.eq("username", name));
        //User user = (User) crit.uniqueResult();
        if(query.getResultList().size() > 0) {
            user = (User) query.getSingleResult();
            if (user.getRoles() == null) {
                Hibernate.initialize(user.getRoles());
            }
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
        Query query = entityManager.createQuery("select u from User u");
        return query.getResultList();

    }
}
