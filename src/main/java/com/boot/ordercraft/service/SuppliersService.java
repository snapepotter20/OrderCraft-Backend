
package com.boot.ordercraft.service;

import com.boot.ordercraft.exception.DuplicateEmailException;
import com.boot.ordercraft.exception.DuplicatePhoneException;
import com.boot.ordercraft.model.Suppliers;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.repository.SuppliersRepository;
import com.boot.ordercraft.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SuppliersService {
	
	private static final String CONTRACTS_DIR = "contracts";

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Check for duplicate email
        suppliersRepository.findByContactEmail(supplier.getContact_email())
                .ifPresent(existing -> {
                    throw new DuplicateEmailException("A supplier with this email already exists!");
                });

        // Check for duplicate phone
        suppliersRepository.findByPhone(supplier.getPhone())
                .ifPresent(existing -> {
                    throw new DuplicatePhoneException("A supplier with this phone already exists!");
                });

        supplier.setUser(existingUser);

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
        existing.setRating(updatedSupplier.getRating());

        return suppliersRepository.save(existing);
    }

    public void deleteSupplier(Long id) {
        if (!suppliersRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
        suppliersRepository.deleteById(id);
    }
    
    public Suppliers getSupplierById(Long id) {
        return suppliersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }
    
    public void uploadContract(Long id, MultipartFile file) throws IOException {
        Suppliers supplier = suppliersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        // Ensure directory exists
        File dir = new File(CONTRACTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Save file with supplierId in name
        String fileName = "supplier_" + id + "_contract.pdf";
        Path filePath = Paths.get(CONTRACTS_DIR, fileName);
        Files.write(filePath, file.getBytes());

        // Save path in DB
        supplier.setContractFile(filePath.toString());
        suppliersRepository.save(supplier);
    }

    public Resource downloadContract(Long id) {
        Suppliers supplier = suppliersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        if (supplier.getContractFile() == null) {
            throw new RuntimeException("No contract uploaded for supplier with id: " + id);
        }

        return new FileSystemResource(supplier.getContractFile());
    }


}
