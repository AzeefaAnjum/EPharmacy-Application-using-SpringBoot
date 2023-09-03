package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.OrderedMedicine;

public interface OrderedMedicineRepository extends CrudRepository<OrderedMedicine, Integer>{

	

}
