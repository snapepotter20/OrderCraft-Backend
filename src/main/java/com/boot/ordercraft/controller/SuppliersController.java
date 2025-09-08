package com.boot.ordercraft.controller;

import com.boot.ordercraft.model.Suppliers;
import com.boot.ordercraft.service.SuppliersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class SuppliersController {

    @Autowired
    private SuppliersService suppliersService;

    // Create a supplier
    @PostMapping("/suppliers/createsupplier")
    public Suppliers createSupplier(@RequestBody Suppliers supplier) {
        return suppliersService.saveSupplier(supplier);
    }

    // Get all suppliers
    @GetMapping("/suppliers/getallsuppliers")
    public List<Suppliers> getAllSuppliers() {
        return suppliersService.getAllSuppliers();
    }
    

    // Update supplier
    @PutMapping("/suppliers/{id}")
    public Suppliers updateSupplier(@PathVariable Long id, @RequestBody Suppliers supplier) {
        return suppliersService.updateSupplier(id, supplier);
    }

    // Delete supplier
    @DeleteMapping("/suppliers/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        suppliersService.deleteSupplier(id);
    }
}
