package org.sid.inventoryservice.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.inventoryservice.entities.Product;
import org.sid.inventoryservice.mappers.PagedResponseMapper;
import org.sid.inventoryservice.records.PagedResponse;
import org.sid.inventoryservice.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final PagedResponseMapper pagedResponseMapper;

    @GetMapping
    public ResponseEntity<PagedResponse<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/products?page={}&size={}", page, size);

        try {
            Page<Product> productsPage = productService.getAllProducts(page, size);
            PagedResponse<Product> response = pagedResponseMapper.toPagedResponse(productsPage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching products", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<PagedResponse<Product>> getAllProductsDefault() {
        log.info("GET /api/products/all");

        try {
            Page<Product> productsPage = productService.getAllProducts();
            PagedResponse<Product> response = pagedResponseMapper.toPagedResponse(productsPage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching products", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("GET /api/products/{}", id);

        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            log.warn("Product not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching product with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        log.info("GET /api/products/name/{}", name);

        try {
            Product product = productService.getProductByName(name);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            log.warn("Product not found with name: {}", name);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching product with name: {}", name, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<Product>> searchProductsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/products/search?name={}&page={}&size={}", name, page, size);

        try {
            Page<Product> productsPage = productService.searchProductsByName(name, page, size);
            PagedResponse<Product> response = pagedResponseMapper.toPagedResponse(productsPage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching products", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/price-range")
    public ResponseEntity<PagedResponse<Product>> getProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/products/price-range?minPrice={}&maxPrice={}&page={}&size={}",
                minPrice, maxPrice, page, size);

        try {
            Page<Product> productsPage = productService.getProductsByPriceRange(minPrice, maxPrice, page, size);
            PagedResponse<Product> response = pagedResponseMapper.toPagedResponse(productsPage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching products by price range", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<PagedResponse<Product>> getLowStockProducts(
            @RequestParam(defaultValue = "5") int threshold,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /api/products/low-stock?threshold={}&page={}&size={}", threshold, page, size);

        try {
            Page<Product> productsPage = productService.getLowStockProducts(threshold, page, size);
            PagedResponse<Product> response = pagedResponseMapper.toPagedResponse(productsPage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching low stock products", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("POST /api/products - Creating new product: {}", product.getName());

        try {
            productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (RuntimeException e) {
            log.warn("Error creating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        log.info("PUT /api/products/{} - Updating product", id);

        try {
            productService.updateProduct(id, product);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            log.warn("Error updating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating product with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<Product> updateProductQuantity(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {

        Integer quantity = request.get("quantity");
        log.info("PATCH /api/products/{}/quantity - Updating quantity to {}", id, quantity);

        try {
            productService.updateProductQuantity(id, quantity);
            Product updatedProduct = productService.getProductById(id);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            log.warn("Error updating product quantity: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error updating product quantity with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{}", id);

        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Error deleting product: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting product with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countProducts() {
        log.info("GET /api/products/count");

        try {
            long count = productService.countProducts();
            return ResponseEntity.ok(Collections.singletonMap("count", count));
        } catch (Exception e) {
            log.error("Error counting products", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inventory/value")
    public ResponseEntity<Map<String, Long>> getTotalInventoryValue() {
        log.info("GET /api/products/inventory/value");

        try {
            long totalValue = productService.getTotalInventoryValue();
            return ResponseEntity.ok(Collections.singletonMap("totalValue", totalValue));
        } catch (Exception e) {
            log.error("Error calculating total inventory value", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inventory/quantity")
    public ResponseEntity<Map<String, Integer>> getTotalInventoryQuantity() {
        log.info("GET /api/products/inventory/quantity");

        try {
            int totalQuantity = productService.getTotalInventoryQuantity();
            return ResponseEntity.ok(Collections.singletonMap("totalQuantity", totalQuantity));
        } catch (Exception e) {
            log.error("Error calculating total inventory quantity", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Map<String, Boolean>> productExists(@PathVariable Long id) {
        log.info("GET /api/products/{}/exists", id);

        try {
            boolean exists = productService.productExists(id);
            return ResponseEntity.ok(Collections.singletonMap("exists", exists));
        } catch (Exception e) {
            log.error("Error checking if product exists with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}