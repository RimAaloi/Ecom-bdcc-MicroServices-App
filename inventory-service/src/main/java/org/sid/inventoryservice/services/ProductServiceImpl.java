package org.sid.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.inventoryservice.entities.Product;
import org.sid.inventoryservice.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<Product> getAllProducts(int page, int size) {
        log.info("Fetching products page {} with size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllProducts() {
        log.info("Fetching all products with default pagination");
        Pageable pageable = PageRequest.of(0, 10);
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public Product getProductByName(String name) {
        log.info("Fetching product with name: {}", name);
        return productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product not found with name: " + name));
    }

    @Override
    public Page<Product> searchProductsByName(String name, int page, int size) {
        log.info("Searching products by name: {}", name);
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<Product> getProductsByPriceRange(double minPrice, double maxPrice, int page, int size) {
        log.info("Fetching products with price between {} and {}", minPrice, maxPrice);
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    @Override
    public Page<Product> getLowStockProducts(int threshold, int page, int size) {
        log.info("Fetching low stock products with threshold: {}", threshold);
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findLowStockProducts(threshold, pageable);
    }

    @Override
    public void saveProduct(Product product) {
        log.info("Saving new product: {}", product.getName());

        // Validation du nom
        if (product.getName() != null && !product.getName().isEmpty()) {
            if (productRepository.existsByName(product.getName())) {
                throw new RuntimeException("Product name already exists: " + product.getName());
            }
        }

        // Validation du prix et quantité
        if (product.getPrice() < 0) {
            throw new RuntimeException("Price cannot be negative");
        }
        if (product.getQuantity() < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }

        productRepository.save(product);
    }

    @Override
    public void updateProduct(Long id, Product product) {
        log.info("Updating product with id: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Vérifier si le nom a changé et s'il existe déjà
        if (!existingProduct.getName().equals(product.getName()) &&
                productRepository.existsByName(product.getName())) {
            throw new RuntimeException("Product name already exists: " + product.getName());
        }

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());

        productRepository.save(existingProduct);
    }

    @Override
    public void updateProductQuantity(Long id, int quantity) {
        log.info("Updating product quantity for id: {} to {}", id, quantity);

        if (quantity < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existingProduct.setQuantity(quantity);
        productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    @Override
    public boolean productExists(Long id) {
        log.info("Checking if product exists with id: {}", id);
        return productRepository.existsById(id);
    }

    @Override
    public boolean productExistsByName(String name) {
        log.info("Checking if product exists with name: {}", name);
        return productRepository.existsByName(name);
    }

    @Override
    public long countProducts() {
        log.info("Counting total products");
        return productRepository.count();
    }

    @Override
    public long getTotalInventoryValue() {
        log.info("Calculating total inventory value");
        return productRepository.findAll().stream()
                .mapToLong(product -> (long) (product.getPrice() * product.getQuantity()))
                .sum();
    }

    @Override
    public int getTotalInventoryQuantity() {
        log.info("Calculating total inventory quantity");
        Long total = productRepository.getTotalInventoryQuantity();
        return total != null ? total.intValue() : 0;
    }
}
