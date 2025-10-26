package org.sid.customerservice.services;

import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.records.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface CustomerService {

    Page<Customer> getAllCustomers(int page, int size);

    Page<Customer> getAllCustomers();

    Customer getCustomerById(String id);

    void saveCustomer(Customer customer);

    void updateCustomer(String id, Customer customer);

    void deleteCustomer(String id);

    boolean customerExists(String id);

    long countCustomers();
}