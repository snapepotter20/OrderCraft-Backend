package com.boot.ordercraft.service;

import com.boot.ordercraft.model.Suppliers;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.repository.SuppliersRepository;
import com.boot.ordercraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuppliersService {

    @Autowired
    private SuppliersRepository suppliersRepository;

    @Autowired
    private UserRepository userRepository;

    public Suppliers saveSupplier(Suppliers supplier) {
        if (supplier.getUser() != null && supplier.getUser().getUser_id() != 0) {
            User existingUser = userRepository.findById(supplier.getUser().getUser_id())
                                              .orElseThrow(() -> new RuntimeException("User not found with id: " + supplier.getUser().getUser_id()));
            supplier.setUser(existingUser);
        } else {
            throw new RuntimeException("User ID must be provided");
        }

        // Address is a new object, will be persisted via cascade
        return suppliersRepository.save(supplier);
    }

    public List<Suppliers> getAllSuppliers() {
        return suppliersRepository.findAll();
    }
}
