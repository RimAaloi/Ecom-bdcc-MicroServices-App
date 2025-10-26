package org.sid.inventoryservice.services;

import org.sid.inventoryservice.entities.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<Product> getAllProducts(int page, int size);
    Page<Product> getAllProducts();
    Product getProductById(Long id);
    Product getProductByName(String name);
    Page<Product> searchProductsByName(String name, int page, int size);
    Page<Product> getProductsByPriceRange(double minPrice, double maxPrice, int page, int size);
    Page<Product> getLowStockProducts(int threshold, int page, int size);
    void saveProduct(Product product);
    void updateProduct(Long id, Product product);
    void updateProductQuantity(Long id, int quantity);
    void deleteProduct(Long id);
    boolean productExists(Long id);
    boolean productExistsByName(String name);
    long countProducts();
    long getTotalInventoryValue();
    int getTotalInventoryQuantity();
}