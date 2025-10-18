package org.sid.inventoryservice;

import org.sid.inventoryservice.entities.Product;
import org.sid.inventoryservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import java.util.List;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository,
                                        RepositoryRestConfiguration restConfiguration) {
        return args -> {
            restConfiguration.exposeIdsFor(Product.class);
              productRepository.saveAll(
                    List.of(
                            Product.builder().name("Laptop").price(1200).quantity(10).build(),
                            Product.builder().name("Phone").price(800).quantity(15).build(),
                            Product.builder().name("Tablet").price(500).quantity(20).build()
                    )
            );

            productRepository.findAll().forEach(p -> {
                System.out.println(p.toString());
            });
        };
    }
}
