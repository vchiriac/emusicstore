package com.emusicstore.service.impl;


import com.emusicstore.dao.CustomerDao;
import com.emusicstore.enums.RoleEnum;
import com.emusicstore.model.Customer;
import com.emusicstore.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public void addCustomer(Customer customer, RoleEnum roleEnum) {
        customerDao.addCustomer(customer, roleEnum);
    }

    @Override
    public Customer getCustomerById(int customerId) {
        return customerDao.getCustomerById(customerId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        return customerDao.getCustomerByUsername(username);
    }

    @Override
    public List<Customer> getAllTemporaryCustomers() {
        return customerDao.getAllTemporaryCustomers();
    }

    @Override
    public void deleteCustomer(int customerId, RoleEnum roleEnum) {
       customerDao.deleteCustomer(customerId, roleEnum);
    }

}
