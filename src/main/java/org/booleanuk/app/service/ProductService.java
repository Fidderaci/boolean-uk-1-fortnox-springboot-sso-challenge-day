package org.booleanuk.app.service;

import org.booleanuk.app.dto.request.ProductRequestDTO;
import org.booleanuk.app.dto.response.ProductResponseDTO;
import org.booleanuk.app.dto.response.ProductSalesResponseDTO;
import org.booleanuk.app.model.Order;
import org.booleanuk.app.model.Product;
import org.booleanuk.app.repo.ProductRepository;
import org.booleanuk.app.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponseDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Product product = new Product();
        product.setName(requestDTO.name());
        product.setPrice(requestDTO.price());

        Product savedProduct = productRepository.save(product);
        return convertToResponseDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        product.setName(requestDTO.name());
        product.setPrice(requestDTO.price());

        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        List<Order> ordersWithProduct = orderRepository.findByProductsContaining(product);
        for (Order order : ordersWithProduct) {
            order.getProducts().removeIf(p -> p.getId().equals(product.getId()));
            orderRepository.save(order);
        }

        productRepository.deleteById(id);
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
    public List<ProductSalesResponseDTO> getProductsWithSalesCount() {
        List<Product> products = productRepository.findAll();

        List<Order> allOrders = orderRepository.findAll();

        return products.stream().map(product -> {
            long soldCount = allOrders.stream()
                    .filter(order -> order.getProducts() != null && order.getProducts().contains(product))
                    .count();

            return new ProductSalesResponseDTO(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    soldCount
            );
        }).collect(Collectors.toList());
    }
}