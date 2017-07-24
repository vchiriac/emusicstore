package com.emusicstore.dao.impl;

import com.emusicstore.dao.AbstractDao;
import com.emusicstore.dao.RoleDao;
import com.emusicstore.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository("roleDao")
@Transactional
public class RoleDaoImpl extends AbstractDao<Integer, Role> implements RoleDao {

    static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Role findByName(String name) {
        logger.info("name : {}", name);
        Role role = null;
        Query query = manager.createQuery("select r from Role r where r.name = :name");
        query.setParameter("name", name);
        if (query.getResultList().size() > 0) {
            role = (Role) query.getSingleResult();
        }

        return role;

    }

    @Override
    public List<Role> findAll() {
        Query query = manager.createQuery("select r from Role r");

        if (query.getResultList().size() > 0) {
            return query.getResultList();
        }
        return null;
    }
}
