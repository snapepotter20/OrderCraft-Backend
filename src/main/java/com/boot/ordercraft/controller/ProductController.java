package com.boot.ordercraft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.model.RawMaterial;
import com.boot.ordercraft.repository.ProductRepository;
import com.boot.ordercraft.repository.RawMaterialRepository;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:53898")
public class ProductController {

	 @Autowired
	 private ProductRepository productRepository;

	 @GetMapping("/getallproducts")
	 public List<Product> getAllRawMaterials() {
	     return productRepository.findAll();
	 }
}
