package org.sid.customerservice.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.mappers.PagedResponseMapper;
import org.sid.customerservice.records.PagedResponse;
import org.sid.customerservice.services.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;
    private final PagedResponseMapper pagedResponseMapper;

    @GetMapping
    public ResponseEntity<PagedResponse<Customer>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/customers?page={}&size={}", page, size);

        try {
            Page<Customer> customersPage = customerService.getAllCustomers(page, size);
            PagedResponse<Customer> response = pagedResponseMapper.toPagedResponse(customersPage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching customers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<PagedResponse<Customer>> getAllCustomersDefault() {
        log.info("GET /api/customers/all - Getting all customers with default pagination");

        try {
            Page<Customer> customersPage = customerService.getAllCustomers();
            PagedResponse<Customer> response = pagedResponseMapper.toPagedResponse(customersPage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching customers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        log.info("GET /api/customers/{}", id);

        try {
            Customer customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            log.warn("Customer not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching customer with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        log.info("POST /api/customers - Creating new customer: {}", customer.getName());

        try {
            customerService.saveCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(customer);
        } catch (RuntimeException e) {
            log.warn("Error creating customer: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating customer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable String id,
            @RequestBody Customer customer) {

        log.info("PUT /api/customers/{} - Updating customer", id);

        try {
            customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            log.warn("Error updating customer: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating customer with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        log.info("DELETE /api/customers/{}", id);

        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Error deleting customer: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting customer with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countCustomers() {
        log.info("GET /api/customers/count");

        try {
            long count = customerService.countCustomers();
            return ResponseEntity.ok(Collections.singletonMap("count", count));
        } catch (Exception e) {
            log.error("Error counting customers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Map<String, Boolean>> customerExists(@PathVariable String id) {
        log.info("GET /api/customers/{}/exists", id);

        try {
            boolean exists = customerService.customerExists(id);
            return ResponseEntity.ok(Collections.singletonMap("exists", exists));
        } catch (Exception e) {
            log.error("Error checking if customer exists with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}