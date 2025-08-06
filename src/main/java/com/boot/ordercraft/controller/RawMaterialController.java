package com.boot.ordercraft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.ordercraft.model.RawMaterial;
import com.boot.ordercraft.model.Role;
import com.boot.ordercraft.repository.RawMaterialRepository;
import com.boot.ordercraft.repository.RoleRepository;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:53898")
public class RawMaterialController {

	 @Autowired
	 private RawMaterialRepository rawMaterialRepository;

	 @GetMapping("/getallrawmaterials")
	 public List<RawMaterial> getAllRawMaterials() {
	     return rawMaterialRepository.findAll();
	 }
	
}
