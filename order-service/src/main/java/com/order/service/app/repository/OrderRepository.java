package com.order.service.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.service.app.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
