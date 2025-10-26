package org.sid.customerservice.services;

import lombok.extern.slf4j.Slf4j;
import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<Customer> getAllCustomers(int page, int size) {
        log.info("Fetching customers page {} with size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> getAllCustomers() {
        log.info("Fetching all customers with default pagination");
        Pageable pageable = PageRequest.of(0, 10);
        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer getCustomerById(String id) {
        log.info("Fetching customer with id: {}", id);
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    public void saveCustomer(Customer customer) {
        log.info("Saving new customer: {}", customer.getName());
        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
            boolean emailExists = customerRepository.findByEmail(customer.getEmail())
                    .isPresent();
            if (emailExists) {
                throw new RuntimeException("Email already exists: " + customer.getEmail());
            }
        }
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(String id, Customer customer) {
        log.info("Updating customer with id: {}", id);
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        // Mettre à jour les champs
        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());

        customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(String id) {
        log.info("Deleting customer with id: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }

        customerRepository.deleteById(id);
    }

    @Override
    public boolean customerExists(String id) {
        log.warn("customerExists with Long id might not work as expected with String ID");
        return false;
    }

    @Override
    public long countCustomers() {
        log.info("Counting total customers");
        return customerRepository.count();
    }
}