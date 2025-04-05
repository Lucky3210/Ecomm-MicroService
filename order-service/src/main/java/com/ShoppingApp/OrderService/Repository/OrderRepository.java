package com.ShoppingApp.OrderService.Repository;

import com.ShoppingApp.OrderService.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
