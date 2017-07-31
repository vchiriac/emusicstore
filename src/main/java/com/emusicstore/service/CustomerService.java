package com.emusicstore.service;


import com.emusicstore.enums.RoleEnum;
import com.emusicstore.model.Customer;

import java.util.List;

public interface CustomerService {

    void addCustomer(Customer customer, RoleEnum roleEnum);

    Customer getCustomerById(int customerId);

    List<Customer> getAllCustomers();

    Customer getCustomerByUsername(String username);

    List<Customer> getAllTemporaryCustomers();

    void deleteCustomer(int customerId, RoleEnum roleEnum);

}
