package com.emusicstore.dao.impl;

import com.emusicstore.dao.CartItemDao;
import com.emusicstore.dao.CustomerDao;
import com.emusicstore.dao.RoleDao;
import com.emusicstore.dao.UserDao;
import com.emusicstore.enums.RoleEnum;
import com.emusicstore.model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class CustomerDaoImpl implements CustomerDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CartItemDao cartItemDao;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void addCustomer(Customer customer, RoleEnum roleEnum) {
        Session session = sessionFactory.getCurrentSession();

        customer.getBillingAddress().setCustomer(customer);
        customer.getShippingAddress().setCustomer(customer);

        session.saveOrUpdate(customer);
        session.saveOrUpdate(customer.getBillingAddress());
        session.saveOrUpdate(customer.getShippingAddress());

        User newUser = new User();
        newUser.setUsername(customer.getUsername());
        newUser.setPassword(encoder.encode(customer.getPassword()));
        newUser.setEnabled(true);

        newUser.setEmail(customer.getCustomerEmail());
        newUser.setCustomerId(customer.getCutomerId());

        Authorities newAuthorities = new Authorities();
        newAuthorities.setUsername(customer.getUsername());
        newAuthorities.setAuthority(roleEnum.name());

        Role newRole = roleDao.findByName(roleEnum.name());

        newRole.getUsers().add(newUser);
        newUser.getRoles().add(newRole);

        session.saveOrUpdate(newUser);
        session.saveOrUpdate(newAuthorities);

        Cart newCart = new Cart();
        newCart.setCustomer(customer);
        customer.setCart(newCart);

        session.saveOrUpdate(customer);
        session.saveOrUpdate(newCart);

        session.flush();
    }

    @Override
    public Customer getCustomerById(int customerId) {
        Session session = sessionFactory.getCurrentSession();
        return (Customer) session.get(Customer.class, customerId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Customer");
        List<Customer> customerList = query.list();

        return customerList;

    }

    @Override
    public List<Customer> getAllTemporaryCustomers() {
        final String username = "guest";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Customer c where c.username like :username");
        query.setString("username", username + "%");
        List<Customer> customerList = query.list();
        return customerList;

    }

    @Override
    public void deleteCustomer(int customerId, RoleEnum roleEnum) {

        Customer customer = entityManager.find(Customer.class, customerId);
        if (customer != null) {

            Role role = roleDao.findByName(roleEnum.name());
            User userToDelete = userDao.findByName(customer.getUsername());

            User user = entityManager.find(User.class, userToDelete.getId());

            role.getUsers().remove(user);
            user.getRoles().remove(role);

            entityManager.remove(user);

            if (customer.getBillingAddress() != null) {
                entityManager.remove(customer.getBillingAddress());
            }
            if (customer.getShippingAddress() != null) {
                entityManager.remove(customer.getShippingAddress());
            }

            Cart cart = customer.getCart();
            if (cart != null) {
                cartItemDao.removeAllCartItems(cart);
            }

            entityManager.remove(customer);

            System.out.println(String.format("customer %s deleted", customerId));

        }

    }

    @Override
    public Customer getCustomerByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Customer where username = ?");
        query.setString(0, username);


        return (Customer) query.uniqueResult();
    }

}
