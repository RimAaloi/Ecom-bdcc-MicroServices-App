package org.sid.billingservice.services;

import org.sid.billingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.web.config.SpringDataJacksonConfiguration;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="INVENTORY-SERVICE")
public interface ProductRestClient {
    @GetMapping(path="/products/{id}")
    Product FindProductById(@PathVariable long id);
    @GetMapping(path="/products")
    PagedModel<Product> AllProducts();
}
