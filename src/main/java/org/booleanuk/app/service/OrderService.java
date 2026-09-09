package org.booleanuk.app.service;

import org.booleanuk.app.dto.request.OrderRequestDTO;
import org.booleanuk.app.dto.response.OrderResponseDTO;
import org.booleanuk.app.model.Customer;
import org.booleanuk.app.model.Order;
import org.booleanuk.app.model.Product;
import org.booleanuk.app.repo.CustomerRepository;
import org.booleanuk.app.repo.OrderRepository;
import org.booleanuk.app.repo.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getAllOrdersSortedByValue() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "totalAmount")).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderResponseDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(requestDTO.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + requestDTO.customerId()));

        List<Product> products = productRepository.findAllById(requestDTO.productIds());

        Order order = new Order();
        order.setTotalAmount(requestDTO.totalAmount());
        order.setCustomer(customer);
        order.setProducts(products);

        Order saved = orderRepository.save(order);
        return convertToResponseDTO(saved);
    }

    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO requestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

        Customer customer = customerRepository.findById(requestDTO.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + requestDTO.customerId()));

        List<Product> products = productRepository.findAllById(requestDTO.productIds());

        order.setTotalAmount(requestDTO.totalAmount());
        order.setCustomer(customer);
        order.setProducts(products);

        Order updated = orderRepository.save(order);
        return convertToResponseDTO(updated);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderResponseDTO convertToResponseDTO(Order order) {
        Long customerId = order.getCustomer() != null ? order.getCustomer().getId() : null;

        List<Long> productIds = order.getProducts() != null
                ? order.getProducts().stream().map(Product::getId).collect(Collectors.toList())
                : List.of();

        return new OrderResponseDTO(
                order.getId(),
                order.getTotalAmount(),
                customerId,
                productIds
        );
    }
}