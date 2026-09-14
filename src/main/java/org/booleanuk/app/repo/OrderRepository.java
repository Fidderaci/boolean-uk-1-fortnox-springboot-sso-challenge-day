package org.booleanuk.app.repo;

import org.booleanuk.app.model.Order;
import org.booleanuk.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByTotalAmountDesc();

    List<Order> findByProductsContaining(Product product);
}