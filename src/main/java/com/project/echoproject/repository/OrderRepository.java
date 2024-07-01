package com.project.echoproject.repository;

import com.project.echoproject.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByOrderNumber(String orderNumber);
    List<Orders> findByBuyerUserId(String userId);

}
