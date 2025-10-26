
package org.sid.inventoryservice.repository;

import org.sid.inventoryservice.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
    Page<Product> findLowStockProducts(@Param("threshold") int threshold, Pageable pageable);

    boolean existsByName(String name);

    @Query("SELECT SUM(p.quantity) FROM Product p")
    Long getTotalInventoryQuantity();
}