package org.booleanuk.app.service;

import org.booleanuk.app.dto.request.CustomerRequestDTO;
import org.booleanuk.app.dto.response.CustomerResponseDTO;
import org.booleanuk.app.model.Customer;
import org.booleanuk.app.model.Order;
import org.booleanuk.app.repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<CustomerResponseDTO> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        Customer customer = new Customer();
        customer.setName(requestDTO.name());
        customer.setEmail(requestDTO.email());

        Customer saved = customerRepository.save(customer);
        return convertToResponseDTO(saved);
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(id).map(c -> {
            c.setName(requestDTO.name());
            c.setEmail(requestDTO.email());
            return customerRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Customer not found with id " + id));

        return convertToResponseDTO(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public Map<String, BigDecimal> getCustomerTotalOrderValues() {
        List<Customer> customers = customerRepository.findAll();
        Map<String, BigDecimal> report = new HashMap<>();

        for (Customer customer : customers) {
            BigDecimal totalSum = BigDecimal.ZERO;
            if (customer.getOrders() != null) {
                for (Order order : customer.getOrders()) {
                    if (order.getTotalAmount() != null) {
                        totalSum = totalSum.add(order.getTotalAmount());
                    }
                }
            }
            report.put(customer.getName(), totalSum);
        }
        return report;
    }

    private CustomerResponseDTO convertToResponseDTO(Customer customer) {
        List<Long> orderIds = customer.getOrders() != null
                ? customer.getOrders().stream().map(Order::getId).collect(Collectors.toList())
                : List.of();

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                orderIds
        );
    }
}