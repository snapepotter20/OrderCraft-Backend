package com.boot.ordercraft.controller;

import com.boot.ordercraft.model.Suppliers;
import com.boot.ordercraft.service.SuppliersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordercraft/suppliers")
@CrossOrigin(origins = "http://localhost:4200")
public class SuppliersController {

    @Autowired
    private SuppliersService suppliersService;

    // Create a supplier
    @PostMapping("/createsupplier")
    public Suppliers createSupplier(@RequestBody Suppliers supplier) {
        return suppliersService.saveSupplier(supplier);
    }

    // Get all suppliers
    @GetMapping("/getallsuppliers")
    public List<Suppliers> getAllSuppliers() {
        return suppliersService.getAllSuppliers();
    }
}
