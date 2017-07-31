package com.emusicstore.scheduler;


import com.emusicstore.enums.RoleEnum;
import com.emusicstore.model.Customer;
import com.emusicstore.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class TemporaryUserRemovalScheduler {

    @Autowired
    private CustomerService customerService;


    private static final Logger log = LoggerFactory.getLogger(TemporaryUserRemovalScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        System.out.println(String.format("The time is now: %s", dateFormat.format(new Date())));

        List<Customer> temporaryCustomers = customerService.getAllTemporaryCustomers();
        for(Customer customer : temporaryCustomers) {
            customerService.deleteCustomer(customer.getCutomerId(), RoleEnum.ROLE_GUEST);
        }
    }
}
