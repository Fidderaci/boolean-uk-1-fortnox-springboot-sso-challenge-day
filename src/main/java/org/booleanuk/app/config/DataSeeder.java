package org.booleanuk.app.config;

import org.booleanuk.app.model.Customer;
import org.booleanuk.app.model.Order;
import org.booleanuk.app.model.Product;
import org.booleanuk.app.repo.CustomerRepository;
import org.booleanuk.app.repo.OrderRepository;
import org.booleanuk.app.repo.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(CustomerRepository customerRepository,
                                   ProductRepository productRepository,
                                   OrderRepository orderRepository) {
        return args -> {
            if (customerRepository.count() > 0) {
                return;
            }

            Customer anna = customerRepository.save(new Customer(null, "Anna Andersson", "anna@example.com", null));
            Customer bjorn = customerRepository.save(new Customer(null, "Björn Borg", "bjorn@example.com", null));
            customerRepository.save(new Customer(null, "Sven Svensson", "sven@example.com", null));

            Product keyboard = productRepository.save(new Product(null, "Keyboard", new BigDecimal("499.00")));
            Product mouse = productRepository.save(new Product(null, "Mouse", new BigDecimal("249.00")));
            Product monitor = productRepository.save(new Product(null, "Monitor", new BigDecimal("1999.00")));
            Product headset = productRepository.save(new Product(null, "Headset", new BigDecimal("799.00")));
            Product webcam = productRepository.save(new Product(null, "Webcam", new BigDecimal("599.00")));

            Order order1 = new Order();
            order1.setCustomer(anna);
            order1.setProducts(List.of(keyboard, mouse));
            order1.setTotalAmount(keyboard.getPrice().add(mouse.getPrice()));
            orderRepository.save(order1);

            Order order2 = new Order();
            order2.setCustomer(anna);
            order2.setProducts(List.of(monitor));
            order2.setTotalAmount(monitor.getPrice());
            orderRepository.save(order2);

            Order order3 = new Order();
            order3.setCustomer(bjorn);
            order3.setProducts(List.of(headset, webcam));
            order3.setTotalAmount(headset.getPrice().add(webcam.getPrice()));
            orderRepository.save(order3);
        };
    }
}