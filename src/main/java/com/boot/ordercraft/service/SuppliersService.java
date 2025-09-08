package com.boot.ordercraft.service;

import com.boot.ordercraft.model.Suppliers;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.repository.SuppliersRepository;
import com.boot.ordercraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuppliersService {

    @Autowired
    private SuppliersRepository suppliersRepository;

    @Autowired
    private UserRepository userRepository;

//    public Suppliers saveSupplier(Suppliers supplier) {
//        if (supplier.getUser() != null && supplier.getUser().getUser_id() != 0) {
//            User existingUser = userRepository.findById(supplier.getUser().getUser_id())
//                                              .orElseThrow(() -> new RuntimeException("User not found with id: " + supplier.getUser().getUser_id()));
//            supplier.setUser(existingUser);
//        } else {
//            throw new RuntimeException("User ID must be provided");
//        }
//
//        // Address is a new object, will be persisted via cascade
//        return suppliersRepository.save(supplier);
//    }
    
    public Suppliers saveSupplier(Suppliers supplier) {
        // get logged-in user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // usually the email/username from JWT

        // fetch user from DB
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // attach user to supplier
        supplier.setUser(existingUser);

        // Address will be persisted via cascade
        return suppliersRepository.save(supplier);
    }

    public List<Suppliers> getAllSuppliers() {
        return suppliersRepository.findAll();
    }
    
    public Suppliers updateSupplier(Long id, Suppliers updatedSupplier) {
        Suppliers existing = suppliersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        existing.setSupplier_name(updatedSupplier.getSupplier_name());
        existing.setContact_name(updatedSupplier.getContact_name());
        existing.setContact_email(updatedSupplier.getContact_email());
        existing.setPhone(updatedSupplier.getPhone());
        existing.setAddress(updatedSupplier.getAddress());

        return suppliersRepository.save(existing);
    }

    public void deleteSupplier(Long id) {
        if (!suppliersRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
        suppliersRepository.deleteById(id);
    }
}
