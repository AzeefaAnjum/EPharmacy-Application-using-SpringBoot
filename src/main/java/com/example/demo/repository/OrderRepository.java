package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Orders;

public interface OrderRepository extends CrudRepository<Orders, Integer>{

	Iterable<Orders> findByCustomerEmailId(String email);

	

}
