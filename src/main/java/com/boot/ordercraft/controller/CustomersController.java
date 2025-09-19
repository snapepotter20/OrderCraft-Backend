package com.boot.ordercraft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.ordercraft.model.Customers;
import com.boot.ordercraft.repository.CustomersRepository;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomersController {
	
	@Autowired
    private  CustomersRepository customerRepository;

    @GetMapping("/getallcustomers")
    public List<Customers> getAllCustomers() {
        return customerRepository.findAll();
    }
}

